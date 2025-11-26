package com.example.applause.service;

import com.example.applause.kafka.EmpInfo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final MimeMessageHelper mimeMessageHelper;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendCommonMail(String subject, String[] toAddress, Map<String, Object> variables, String template) {

        try{
            mimeMessageHelper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);

            MimeMessage mimeMessage = mimeMessageHelper.getMimeMessage();
            String htmlTemplate = templateEngine.process(template,context);

            mimeMessageHelper.setText(htmlTemplate,true);
            mimeMessageHelper.setTo(toAddress);

            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            System.err.println("Can't send mail");
        }
    }

    @Async
    public void sendIndividualMail(String subject, String toAddress, Map<String, Object> variables, String template) {

        try{
            mimeMessageHelper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);

            MimeMessage mimeMessage = mimeMessageHelper.getMimeMessage();
            String htmlTemplate = templateEngine.process(template,context);

            mimeMessageHelper.setText(htmlTemplate,true);
            mimeMessageHelper.setTo(toAddress);

            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            System.err.println("Can't send mail");
        }
    }
}
