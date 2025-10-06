package com.example.notification.service;

import com.example.notification.email.EmailService;
import com.example.notification.kafka.EmpInfo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    @KafkaListener(topics = "Emp-Comm")
    public void listenTopic(EmpInfo empInfo) throws MessagingException {
        System.out.println("Inside kafka Consumer");
        System.out.println("Emp infos:: "+empInfo.username());

        // send email
        emailService.sendMail(empInfo);
    }
}
