package com.example.award.service;

import com.example.award.dto.AwardDistDto;
import com.example.award.dto.EmpInfo;
import com.example.award.feignClients.EmployeeClient;
import com.example.award.model.AwardDistribution;
import com.example.award.model.AwardType;
import com.example.award.repo.AwardDistRepo;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardDistService {

    private final AwardDistRepo awardDistRepo;
    private final AwardService awardService;
    private final EmployeeClient employeeClient;


    public List<AwardDistribution> distributeAwards(AwardDistDto awardDistDto, String authToken) {

        AwardType awardType = awardService.getAward(awardDistDto.getAwardId());
        List<AwardDistribution> awardDistributionList;

        String category = awardType.getCategory().toLowerCase();
        if(category.equals("individual")){

            awardDistributionList = getAwardDistributionList(awardDistDto,authToken);

        }else if(category.equals("department")){

            JsonNode deptDetails = employeeClient.getDeptDetails(awardDistDto.getDeptId(),authToken);
            JsonNode memberDetails = deptDetails.path("departmentMemberDetails");
            awardDistributionList = getAwardDistributionList(awardDistDto,memberDetails);

        }else if(category.equals("team")){

            JsonNode teamDetails = employeeClient.getTeamDetails(awardDistDto.getTeamId(),authToken);
            JsonNode memberDetails = teamDetails.path("teamMemberDetails");
            awardDistributionList = getAwardDistributionList(awardDistDto,memberDetails);

        }else{
            System.out.println("No proper category");
            throw new IllegalArgumentException("Category!!");
        }

        return awardDistRepo.saveAll(awardDistributionList);
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

    private List<AwardDistribution> getAwardDistributionList(AwardDistDto awardDistDto, JsonNode memberDetails){

        List<AwardDistribution> awardDistributionList = new ArrayList<>();
        AwardType awardType = awardService.getAward(awardDistDto.getAwardId());

        for(JsonNode member : memberDetails){
            AwardDistribution awardDistribution = new AwardDistribution();
            awardDistribution.setAwardName(awardType.getName());
            awardDistribution.setDescription(awardType.getDescription());
            awardDistribution.setPoints(awardType.getPoints());
            awardDistribution.setEmployeeName(member.path("Member Name").asText());
            awardDistribution.setEmployeeId(member.path("Member Id").asInt());
            awardDistribution.setEmpUuid(member.path("Member Uuid").asText());

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
}
