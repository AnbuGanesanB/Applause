package com.example.applause.config;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final JavaMailSender mailSender;

    @Bean
    public MimeMessageHelper getMimeMessageHelper() throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
            new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, UTF_8.name());

        mimeMessageHelper.setFrom("ProductSupport@samplemail.devenv.com");

        return mimeMessageHelper;
    }
}
