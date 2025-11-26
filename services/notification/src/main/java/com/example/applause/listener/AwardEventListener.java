package com.example.applause.listener;

import com.example.applause.feignClients.EmployeeClient;
import com.example.applause.kafka.DeptAwardDistributedEvent;
import com.example.applause.kafka.EmpInfo;
import com.example.applause.kafka.IndlAwardDistributedEvent;
import com.example.applause.kafka.TeamAwardDistributedEvent;
import com.example.applause.service.AwardNotificationService;
import com.example.applause.service.EmailService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@KafkaListener(
        topics = "award-events-v0",
        groupId = "notificationGroupV4",
        containerFactory = "multiTypeKafkaListenerContainerFactory"
)
public class AwardEventListener {

    private final EmployeeClient employeeClient;
    private final ObjectMapper objectMapper;
    private final AwardNotificationService awardNotificationService;

    @KafkaHandler
    public void handleIndividualAwardNotification(IndlAwardDistributedEvent event) {

        Map<String, Object> awardMap = Map.of(
                "name", event.getAwardName(),
                "description", event.getAwardDescription(),
                "points", event.getRewardPoints()
        );

        for(String uuid: event.getEmpUuids()){
            EmpInfo empInfo = employeeClient.getEmployeeDetails(uuid);
            awardNotificationService.sendIndividualAwardNotificationMail(empInfo,awardMap);
        }
    }

    @KafkaHandler
    public void handleDeptAwardNotification(DeptAwardDistributedEvent event) {

        Map<String, Object> awardMap = Map.of(
                "name", event.getAwardName(),
                "description", event.getAwardDescription(),
                "points", event.getRewardPoints()
        );

        Map<String, Object> deptDetails = employeeClient.getDeptDetails(event.getDepartmentId());
        Object memberDetailsObj = deptDetails.get("departmentMemberDetails");
        List<EmpInfo> memberDetails = objectMapper.convertValue(memberDetailsObj,new TypeReference<List<EmpInfo>>() {});

        String departmentName = (String)deptDetails.get("departmentName");

        //Send Common Mail first
        awardNotificationService.sendGroupAwardNotificationMail(memberDetails,awardMap,"department", departmentName);

        //Send Individual Mail Next
        for(EmpInfo empInfo: memberDetails){
            awardNotificationService.sendIndividualAwardNotificationMail(empInfo,awardMap);
        }
    }

    @KafkaHandler
    public void handleTeamAwardNotification(TeamAwardDistributedEvent event) {

        Map<String, Object> awardMap = Map.of(
                "name", event.getAwardName(),
                "description", event.getAwardDescription(),
                "points", event.getRewardPoints()
        );

        Map<String, Object> teamDetails = employeeClient.getTeamDetails(event.getTeamId());
        Object memberDetailsObj = teamDetails.get("teamMemberDetails");
        List<EmpInfo> memberDetails = objectMapper.convertValue(memberDetailsObj,new TypeReference<List<EmpInfo>>() {});

        String teamName = (String)teamDetails.get("teamName");

        //Send Common Mail first
        awardNotificationService.sendGroupAwardNotificationMail(memberDetails,awardMap,"team",teamName);

        //Send Individual Mail Next
        for(EmpInfo empInfo: memberDetails){
            awardNotificationService.sendIndividualAwardNotificationMail(empInfo,awardMap);
        }
    }
}
