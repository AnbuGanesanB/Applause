package com.example.applause.service;

import com.example.applause.kafka.EmpInfo;
import com.example.applause.kafka.GoodyOrderedEvent;
import com.example.applause.kafka.OrderCancelledEvent;
import com.example.applause.kafka.OrderDeliveredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoodyNotificationService {

    private final EmailService emailService;
    private final DateTimeFormatter dateTimeFormatter;

    public void sendOrderConfirmationMail(EmpInfo empInfo, GoodyOrderedEvent event){
        String email = empInfo.email();
        String eventTime = event.getDateTime().format(dateTimeFormatter);

        Map<String, Object> goodyMap = Map.of(
                "name",event.getGoodyName(),
                "quantity",event.getQty(),
                "orderTime",eventTime
        );

        Map<String, Object> variables = new HashMap<>();
        variables.put("goody", goodyMap);
        variables.put("name",empInfo.empName());

        emailService.sendIndividualMail("Order Confirmation",email,variables,"goody_ordered_Template.html");
    }

    public void sendOrderCancelledMail(EmpInfo empInfo, OrderCancelledEvent event){
        String email = empInfo.email();
        String eventTime = event.getDateTime().format(dateTimeFormatter);

        Map<String, Object> goodyMap = Map.of(
                "name",event.getGoodyName(),
                "quantity",event.getQty(),
                "cancelTime",eventTime
        );

        Map<String, Object> variables = new HashMap<>();
        variables.put("goody", goodyMap);
        variables.put("name",empInfo.empName());

        emailService.sendIndividualMail("Order Cancel Confirmation",email,variables,"goody_orderCancelled_Template.html");
    }

    public void sendOrderDeliveredMail(EmpInfo empInfo, OrderDeliveredEvent event){
        String email = empInfo.email();
        String eventTime = event.getDateTime().format(dateTimeFormatter);

        Map<String, Object> goodyMap = Map.of(
                "name",event.getGoodyName(),
                "quantity",event.getQty(),
                "deliveryTime",eventTime
        );

        Map<String, Object> variables = new HashMap<>();
        variables.put("goody", goodyMap);
        variables.put("name",empInfo.empName());

        emailService.sendIndividualMail("Order Delivery Confirmation",email,variables,"goody_orderDelivered_Template.html");
    }
}
