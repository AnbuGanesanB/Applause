package com.example.applause.listener;

import com.example.applause.feignClients.EmployeeClient;
import com.example.applause.kafka.EmpInfo;
import com.example.applause.kafka.GoodyOrderedEvent;
import com.example.applause.kafka.OrderCancelledEvent;
import com.example.applause.kafka.OrderDeliveredEvent;
import com.example.applause.service.GoodyNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(
        topics = "goody-events-v3",
        groupId = "notificationGroupV4",
        containerFactory = "multiTypeKafkaListenerContainerFactory"
)
public class GoodyEventListener {

    private final EmployeeClient employeeClient;
    private final GoodyNotificationService goodyNotificationService;

    @KafkaHandler
    public void handleGoodyOrdered(GoodyOrderedEvent event) {
        EmpInfo empInfo = employeeClient.getEmployeeDetails(event.getEmpUuid());
        goodyNotificationService.sendOrderConfirmationMail(empInfo, event);
    }

    @KafkaHandler
    public void handleOrderDelivered(OrderDeliveredEvent event) {
        EmpInfo empInfo = employeeClient.getEmployeeDetails(event.getEmpUuid());
        goodyNotificationService.sendOrderDeliveredMail(empInfo,event);
    }

    @KafkaHandler
    public void handleOrderCancelled(OrderCancelledEvent event) {
        EmpInfo empInfo = employeeClient.getEmployeeDetails(event.getEmpUuid());
        goodyNotificationService.sendOrderCancelledMail(empInfo,event);
    }

    @KafkaHandler(isDefault = true)
    public void handleUnknown(Object unknown) {
        System.out.println("Unknown event: " + unknown.getClass());
        System.out.println(unknown.toString());
    }

}
