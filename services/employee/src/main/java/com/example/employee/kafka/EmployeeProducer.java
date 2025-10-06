package com.example.employee.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
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
