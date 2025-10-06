package com.example.notification.email;

import com.example.notification.kafka.EmpInfo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(EmpInfo empInfo) throws MessagingException {
        System.out.println("In Mail Send Method");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, UTF_8.name());

        mimeMessageHelper.setFrom("anbuganesh@pdmrindia.com");
        mimeMessageHelper.setSubject("Sample");

        Map<String, Object> variables = new HashMap<>();
        variables.put("EmpName",empInfo.username());
        variables.put("Email",empInfo.email());

        Context context = new Context();
        context.setVariables(variables);

        try{
            String htmlTemplate = templateEngine.process("mail-template.html",context);
            mimeMessageHelper.setText(htmlTemplate,true);

            mimeMessageHelper.setTo("sarath@pdmrindia.com");
            mailSender.send(mimeMessage);
            System.out.println("Sent mail to Sarath");
        }catch (MessagingException e){
            System.err.println("Can't send mail");
        }
    }
}
