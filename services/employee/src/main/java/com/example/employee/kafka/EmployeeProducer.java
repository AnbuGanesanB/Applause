package com.example.employee.kafka;

import com.example.employee.dtos.EmpInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeProducer{

    private final KafkaTemplate<String, EmpInfo> kafkaTemplate;

    public void sendEmpData(EmpInfo empInfo){
        System.out.println("Sending the Data");

        Message<EmpInfo> message = MessageBuilder
                .withPayload(empInfo)
                .setHeader(KafkaHeaders.TOPIC,"Emp-Comm")
                .build();

        kafkaTemplate.send(message);
    }
}
