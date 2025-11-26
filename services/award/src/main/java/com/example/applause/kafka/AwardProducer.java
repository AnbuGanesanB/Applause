package com.example.applause.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.example.applause.config.ApiConstant.kafka_topic;

@Service
@RequiredArgsConstructor
public class AwardProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendIndlAwardDistributedEvent(IndlAwardDistributedEvent indlAwardDistributedEvent){

        Message<Object> message = MessageBuilder
                .withPayload((Object) indlAwardDistributedEvent)
                .setHeader("__TypeId__", "indlAwardDistributedEvent")
                .setHeader(KafkaHeaders.TOPIC,kafka_topic)
                .build();

        kafkaTemplate.send(message);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendDeptAwardDistributedEvent(DeptAwardDistributedEvent deptAwardDistributedEvent){

        System.out.println("At Producer");
        System.out.println(deptAwardDistributedEvent.getAwardName());
        System.out.println(deptAwardDistributedEvent.getRewardPoints());
        System.out.println(deptAwardDistributedEvent.getDepartmentId());
        Message<Object> message = MessageBuilder
                .withPayload((Object) deptAwardDistributedEvent)
                .setHeader("__TypeId__", "deptAwardDistributedEvent")
                .setHeader(KafkaHeaders.TOPIC,kafka_topic)
                .build();

        kafkaTemplate.send(message);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendTeamAwardDistributedEvent(TeamAwardDistributedEvent teamAwardDistributedEvent){

        Message<Object> message = MessageBuilder
                .withPayload((Object) teamAwardDistributedEvent)
                .setHeader("__TypeId__", "teamAwardDistributedEvent")
                .setHeader(KafkaHeaders.TOPIC,kafka_topic)
                .build();

        kafkaTemplate.send(message);
    }
}
