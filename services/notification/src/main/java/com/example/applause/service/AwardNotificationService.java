package com.example.applause.service;

import com.example.applause.kafka.EmpInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AwardNotificationService {

    private final EmailService emailService;

    public void sendIndividualAwardNotificationMail(EmpInfo empInfo, Map<String, Object> awardMap){
        String email = empInfo.email();

        Map<String, Object> variables = new HashMap<>();
        variables.put("award", awardMap);
        variables.put("awardee",empInfo.empName());

        emailService.sendIndividualMail((String) awardMap.get("name"),email,variables,"indl_award_Template.html");
    }

    public void sendGroupAwardNotificationMail(List<EmpInfo> memberDetails, Map<String, Object> awardMap, String groupIdentifier, String groupName){
        String[] toAddress = memberDetails.stream()
                .map(EmpInfo::email)
                .toArray(String[]::new);

        Map<String, Object> variables = new HashMap<>();
        variables.put("award", awardMap);
        variables.put("teamName",groupName);

        String subject = "";

        if(groupIdentifier.contains("department")){
            subject = "Congrats "+groupName+ " Dept";

        } else if (groupIdentifier.contains("team")) {
            subject = "Congrats "+groupName+ " Team";
        }

        emailService.sendCommonMail(subject,toAddress,variables,"common_award_notify_template.html");
    }
}
