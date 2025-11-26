package com.example.applause.service;

import com.example.applause.dto.AwardDistDto;
import com.example.applause.dto.EmpInfo;
import com.example.applause.feignClients.EmployeeClient;
import com.example.applause.kafka.DeptAwardDistributedEvent;
import com.example.applause.kafka.IndlAwardDistributedEvent;
import com.example.applause.kafka.TeamAwardDistributedEvent;
import com.example.applause.model.AwardDistribution;
import com.example.applause.model.AwardType;
import com.example.applause.repo.AwardDistRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardDistService {

    private final AwardDistRepo awardDistRepo;
    private final AwardService awardService;
    private final EmployeeClient employeeClient;
    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;


    @Transactional
    public List<AwardDistribution> distributeAwards(AwardDistDto awardDistDto, String authToken) {

        AwardType awardType = awardService.getAward(awardDistDto.getAwardId());
        List<AwardDistribution> awardDistributionList;

        String category = awardType.getCategory().toLowerCase();
        if(category.equals("individual")){

            awardDistributionList = getAwardDistributionList(awardDistDto,authToken);

            awardDistributionList = awardDistRepo.saveAll(awardDistributionList);
            publisher.publishEvent(getIndlAwardDistributedEvent(awardDistributionList,awardType));

        }else if(category.equals("department")){

            Map<String, Object> deptDetails = employeeClient.getDeptDetails(awardDistDto.getDeptId(),authToken);
            Object memberDetailsObj = deptDetails.get("departmentMemberDetails");
            List<EmpInfo> memberDetails = objectMapper.convertValue(memberDetailsObj,new TypeReference<List<EmpInfo>>() {});

            awardDistributionList = getAwardDistributionList(awardDistDto,memberDetails);

            awardDistributionList = awardDistRepo.saveAll(awardDistributionList);
            publisher.publishEvent(getDeptAwardDistributedEvent(awardType,awardDistDto.getDeptId()));

        }else if(category.equals("team")){

            Map<String, Object> teamDetails = employeeClient.getTeamDetails(awardDistDto.getTeamId(),authToken);
            Object memberDetailsObj = teamDetails.get("teamMemberDetails");
            List<EmpInfo> memberDetails = objectMapper.convertValue(memberDetailsObj,new TypeReference<List<EmpInfo>>() {});

            awardDistributionList = getAwardDistributionList(awardDistDto,memberDetails);

            awardDistributionList = awardDistRepo.saveAll(awardDistributionList);
            publisher.publishEvent(getTeamAwardDistributedEvent(awardType,awardDistDto.getTeamId()));

        }else{
            System.out.println("No proper category");
            throw new IllegalArgumentException("Category!!");
        }

        return awardDistributionList;
    }

    private List<AwardDistribution> getAwardDistributionList(AwardDistDto awardDistDto, String authToken){

        List<AwardDistribution> awardDistributionList = new ArrayList<>();
        AwardType awardType = awardService.getAward(awardDistDto.getAwardId());

        List<Integer> awardeesId = awardDistDto.getAwardees();

        awardeesId.forEach(awardeeId->{
            EmpInfo awardee = employeeClient.getEmployee(awardeeId,authToken);

            AwardDistribution awardDistribution = new AwardDistribution();
            awardDistribution.setAwardName(awardType.getName());
            awardDistribution.setDescription(awardType.getDescription());
            awardDistribution.setPoints(awardType.getPoints());
            awardDistribution.setEmployeeName(awardee.empName());
            awardDistribution.setEmployeeId(awardee.id());
            awardDistribution.setEmpUuid(awardee.empUuid());

            awardDistributionList.add(awardDistribution);
        });

        return awardDistributionList;
    }

    private List<AwardDistribution> getAwardDistributionList(AwardDistDto awardDistDto, List<EmpInfo> memberDetails){

        List<AwardDistribution> awardDistributionList = new ArrayList<>();
        AwardType awardType = awardService.getAward(awardDistDto.getAwardId());

        for(EmpInfo member : memberDetails){
            AwardDistribution awardDistribution = new AwardDistribution();
            awardDistribution.setAwardName(awardType.getName());
            awardDistribution.setDescription(awardType.getDescription());
            awardDistribution.setPoints(awardType.getPoints());
            awardDistribution.setEmployeeName(member.empName());
            awardDistribution.setEmployeeId(member.id());
            awardDistribution.setEmpUuid(member.empUuid());

            awardDistributionList.add(awardDistribution);
        }
        return awardDistributionList;
    }

    @Transactional
    public void syncEmpUuid(String authToken) {

        List<AwardDistribution> distibutionList = awardDistRepo.findByEmpUuidIsNull();

        if(!distibutionList.isEmpty()){

            List<EmpInfo> employeeList = employeeClient.getAllEmployees(authToken);
            Map<Integer, String> empIdToUuidMap = employeeList.stream()
                    .filter(e->e.id()!=0 && e.empUuid()!=null)
                    .collect(Collectors.toMap(EmpInfo::id, EmpInfo::empUuid));

            for(AwardDistribution awardDistribution: distibutionList){
                awardDistribution.setEmpUuid(empIdToUuidMap.get(awardDistribution.getEmployeeId()));
            }

            awardDistRepo.saveAll(distibutionList);
        }
    }

    public List<AwardDistribution> getUserAwards(String userUuid) {
        return awardDistRepo.findByEmpUuid(userUuid);
    }

    public List<AwardDistribution> getUserAwards(int empId) {
        return awardDistRepo.findByEmployeeId(empId);
    }

    public int getUserPoints(String userUuid) {
        return Optional.ofNullable(awardDistRepo.findTotalPointsByEmpUuidNative(userUuid)).orElse(0);
    }

    private IndlAwardDistributedEvent getIndlAwardDistributedEvent(List<AwardDistribution> awardDistributionList, AwardType awardType){
        IndlAwardDistributedEvent indlAwardDistributedEvent = new IndlAwardDistributedEvent();

        indlAwardDistributedEvent.setAwardName(awardType.getName());
        indlAwardDistributedEvent.setAwardDescription(awardType.getDescription());
        indlAwardDistributedEvent.setRewardPoints(awardType.getPoints());

        List<String> empUuids = awardDistributionList.stream().map(AwardDistribution::getEmpUuid).collect(Collectors.toList());
        indlAwardDistributedEvent.setEmpUuids(empUuids);

        return indlAwardDistributedEvent;
    }

    private DeptAwardDistributedEvent getDeptAwardDistributedEvent(AwardType awardType, int deptId){
        DeptAwardDistributedEvent deptAwardDistributedEvent = new DeptAwardDistributedEvent();

        deptAwardDistributedEvent.setAwardName(awardType.getName());
        System.out.println("---------"+awardType.getName());
        deptAwardDistributedEvent.setAwardDescription(awardType.getDescription());
        System.out.println("---------"+awardType.getDescription());
        deptAwardDistributedEvent.setRewardPoints(awardType.getPoints());
        System.out.println("---------"+awardType.getPoints());
        deptAwardDistributedEvent.setDepartmentId(deptId);
        System.out.println("---------"+deptId);

        return deptAwardDistributedEvent;
    }

    private TeamAwardDistributedEvent getTeamAwardDistributedEvent(AwardType awardType, int teamId){
        TeamAwardDistributedEvent teamAwardDistributedEvent = new TeamAwardDistributedEvent();

        teamAwardDistributedEvent.setAwardName(awardType.getName());
        teamAwardDistributedEvent.setAwardDescription(awardType.getDescription());
        teamAwardDistributedEvent.setRewardPoints(awardType.getPoints());
        teamAwardDistributedEvent.setTeamId(teamId);

        return teamAwardDistributedEvent;
    }

}