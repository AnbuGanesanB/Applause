package com.example.applause.service;

import com.example.applause.config.GoodyLockManager;
import com.example.applause.dto.EmpInfo;
import com.example.applause.dto.OrderDto;
import com.example.applause.exception.GoodyException;
import com.example.applause.feignClients.AwardClient;
import com.example.applause.feignClients.EmployeeClient;
import com.example.applause.kafka.GoodyOrderedEvent;
import com.example.applause.kafka.OrderCancelledEvent;
import com.example.applause.kafka.OrderDeliveredEvent;
import com.example.applause.model.Goody;
import com.example.applause.model.GoodyDistribution;
import com.example.applause.model.OrderStatus;
import com.example.applause.repo.GoodyDistRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodyDistributionService {

    private final GoodyDistRepo goodyDistRepo;
    private final GoodyService goodyService;
    private final GoodyLockManager goodyLockManager;

    private final AwardClient awardClient;
    private final EmployeeClient employeeClient;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public GoodyDistribution processNewOrder(Jwt jwt, OrderDto orderDto){
        return goodyLockManager.executeWithLock(orderDto.goodyId(), ()->{
            int requiredQty = orderDto.requiredQty();
            int goodyId = orderDto.goodyId();
            String empUuid = getEmpUuid(jwt);

            int totalAwardedPoints = awardClient.getTotalPoints("Bearer "+jwt.getTokenValue());
            int spentPoints = getSpentPoints(empUuid);
            int availablePoints = totalAwardedPoints - spentPoints;

            Goody goody = goodyService.getGoodyDetails(goodyId);
            int requiredPoints = goody.getPoints() * requiredQty;

            if (requiredPoints > availablePoints || requiredQty > goody.getAvailableQuantity()) {
                return null;
            }

            //Wait To test concurrency
            customWait();

            goodyService.decreaseStockByQty(goody, requiredQty);

            //Notify Admin for New order
            GoodyDistribution goodyDistribution = orderNewGoodies(empUuid, requiredQty, goody);

            //Publish Notification
            publisher.publishEvent(new GoodyOrderedEvent(
                    goodyDistribution.getEmpUuid(), goodyDistribution.getGoodyName(), goodyDistribution.getQty(), goodyDistribution.getOrderingTime()
            ));

            return goodyDistribution;
        });
    }

    @Transactional
    private GoodyDistribution orderNewGoodies(String empUuid, int requiredQty, Goody goody){
        GoodyDistribution goodyDistribution = new GoodyDistribution();
        goodyDistribution.setOrderingTime(LocalDateTime.now());
        goodyDistribution.setStatus(OrderStatus.ORDERED);
        goodyDistribution.setEmpUuid(empUuid);
        goodyDistribution.setQty(requiredQty);
        goodyDistribution.setPoints(goody.getPoints() * requiredQty);
        goodyDistribution.setGoodyName(goody.getName());
        goodyDistribution.setGoodyId(goody.getId());

        return goodyDistRepo.save(goodyDistribution);
    }

    @Transactional
    public GoodyDistribution deliverGoodies(int gdId){
        GoodyDistribution goodyDistribution = getGoodyDistributionById(gdId);

        if(goodyDistribution==null) return null;

        goodyDistribution.setStatus(OrderStatus.DELIVERED);
        goodyDistribution.setDeliveredTime(LocalDateTime.now());
        goodyDistribution = goodyDistRepo.save(goodyDistribution);

        //Publish Notification
        publisher.publishEvent(new OrderDeliveredEvent(
                goodyDistribution.getEmpUuid(), goodyDistribution.getGoodyName(), goodyDistribution.getQty(), goodyDistribution.getDeliveredTime()
        ));

        return goodyDistribution;
    }

    @Transactional
    public GoodyDistribution cancelGoodies(int gdId){
        GoodyDistribution goodyDistribution = getGoodyDistributionById(gdId);

        if(goodyDistribution==null) return null;

        Goody goody = getGoodyByName(goodyDistribution.getGoodyName());
        if(goody!=null && shallTakeBackStock(goodyDistribution)){
            goodyService.increaseStockByQty(goody,goodyDistribution.getQty());
        }
        goodyDistribution.setStatus(OrderStatus.CANCELLED);
        goodyDistribution.setCancelledTime(LocalDateTime.now());
        goodyDistribution = goodyDistRepo.save(goodyDistribution);

        //Publish Notification
        publisher.publishEvent(new OrderCancelledEvent(
                goodyDistribution.getEmpUuid(), goodyDistribution.getGoodyName(), goodyDistribution.getQty(), goodyDistribution.getCancelledTime()
        ));

        return goodyDistribution;
    }

    @Transactional
    public GoodyDistribution rejectGoodies(int gdId){
        GoodyDistribution goodyDistribution = getGoodyDistributionById(gdId);

        if(goodyDistribution==null) return null;

        Goody goody = getGoodyByName(goodyDistribution.getGoodyName());
        if(goody!=null && shallTakeBackStock(goodyDistribution)){
            goodyService.increaseStockByQty(goody,goodyDistribution.getQty());
        }
        goodyDistribution.setStatus(OrderStatus.REJECTED);
        goodyDistribution.setRejectedTime(LocalDateTime.now());

        // Notify Employee
        return goodyDistRepo.save(goodyDistribution);
    }

    public List<GoodyDistribution> getAllDistribution(){
        return goodyDistRepo.findAll();
    }

    public List<GoodyDistribution> getAllOwnDistribution(Jwt jwt){
        String empUuid = getEmpUuid(jwt);
        return goodyDistRepo.findAllByEmpUuid(empUuid);
    }

    public List<GoodyDistribution> getAllDistributionOfEmp(int empId){
        return goodyDistRepo.findAllByEmployeeId(empId);
    }

    public void syncDetails(String authToken) {
        List<GoodyDistribution> incompleteRecords = goodyDistRepo.findRecordsWithNoEmpId();

        Map<String,List<GoodyDistribution>> incompleteGroupedRecords = incompleteRecords
                .stream().collect(Collectors.groupingBy(GoodyDistribution::getEmpUuid));

        for(Map.Entry<String,List<GoodyDistribution>> entry: incompleteGroupedRecords.entrySet()){
            String empUuid = entry.getKey();
            List<GoodyDistribution> goodyDistributionList = entry.getValue();

            EmpInfo empInfo = employeeClient.getEmployeeDetails(empUuid,authToken);
            goodyDistributionList.forEach((g)->{
                g.setEmployeeId(empInfo.id());
                g.setEmployeeName(empInfo.empName());
            });
            goodyDistRepo.saveAll(goodyDistributionList);
        }

    }

    private String getEmpUuid(Jwt jwt){
        return jwt.getClaim("sub").toString();
    }

    private int getSpentPoints(String userUuid){
        return Optional.ofNullable(goodyDistRepo.findTotalPointsToOrderByEmpUuid(userUuid)).orElse(0);
    }

    private Goody getGoodyByName(String goodyName){
        Optional<Goody> optionalGoody = goodyService.getGoodyByName(goodyName);
        return optionalGoody.orElse(null);
    }

    private GoodyDistribution getGoodyDistributionById(int id){
        Optional<GoodyDistribution> optGoody = goodyDistRepo.findById(id);
        return optGoody.orElseThrow(()->new GoodyException.NoSuchOrderDistributionException("Pls Check Distribution ID"));
    }

    public int getAvailableRewardPoints(Jwt jwt) {
        int totalAwardedPoints = awardClient.getTotalPoints("Bearer "+jwt.getTokenValue());
        int spentPoints = getSpentPoints(getEmpUuid(jwt));
        return (totalAwardedPoints - spentPoints);
    }

    private boolean shallTakeBackStock(GoodyDistribution goodyDistribution){
        OrderStatus oldStatus = goodyDistribution.getStatus();
        if(oldStatus==OrderStatus.ORDERED) return true;
        return false;
    }

    //Wait To test concurrency
    private void customWait(){
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
