package com.techbank.account.cmd.infrastructure;

import com.techbank.cqrs.core.events.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import producers.EventProducer;

@Service
public class AccountEventProduceer implements EventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void produce(String topic, BaseEvent event) {
        this.kafkaTemplate.send(topic, event);
    }
}
