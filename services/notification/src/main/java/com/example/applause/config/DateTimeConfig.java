package com.example.applause.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeConfig {

    /*@Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.modules(new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }*/

    @Bean
    public DateTimeFormatter getFormattedTime(){
        return DateTimeFormatter.ofPattern("dd-MMM-yyyy hh.mm a");
    }
}
