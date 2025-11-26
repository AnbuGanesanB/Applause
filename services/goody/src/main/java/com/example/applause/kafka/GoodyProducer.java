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
public class GoodyProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderDeliveredMsg(OrderDeliveredEvent orderDeliveredEvent){

        Message<Object> message = MessageBuilder
                .withPayload((Object) orderDeliveredEvent)
                .setHeader("__TypeId__", "orderDeliveredEvent")
                .setHeader(KafkaHeaders.TOPIC,kafka_topic)
                .build();

        kafkaTemplate.send(message);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendGoodyOrderedMsg(GoodyOrderedEvent goodyOrderedEvent){

        Message<Object> message = MessageBuilder
                .withPayload((Object) goodyOrderedEvent)
                .setHeader("__TypeId__", "goodyOrderedEvent")
                .setHeader(KafkaHeaders.TOPIC,kafka_topic)
                .build();

        kafkaTemplate.send(message);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderCancelledMsg(OrderCancelledEvent orderCancelledEvent){

        Message<Object> message = MessageBuilder
                .withPayload((Object) orderCancelledEvent)
                .setHeader("__TypeId__", "orderCancelledEvent")
                .setHeader(KafkaHeaders.TOPIC,kafka_topic)
                .build();

        kafkaTemplate.send(message);
    }


}
