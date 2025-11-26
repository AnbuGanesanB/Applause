package com.example.applause.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static com.example.applause.config.ApiConstant.kafka_topic;

@Configuration
@RequiredArgsConstructor
public class KafkaAwardTopicConfig {

    @Bean
    public NewTopic employeeAdded(){
        return TopicBuilder.name(kafka_topic).build();
    }

    /*@Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }*/
}
