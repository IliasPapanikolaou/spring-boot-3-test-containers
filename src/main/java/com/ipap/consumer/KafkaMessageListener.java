package com.ipap.consumer;

import com.ipap.dto.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaMessageListener {

    @KafkaListener(topics = "testcontainer-demo-topic-1",groupId = "tc-demo-group")
    public void consumeMessage(String message) {
        log.info("Consumer consumes the message {} ", message);
    }

    @KafkaListener(topics = "testcontainer-demo-topic-2",groupId = "tc-demo-group")
    public void consumeEvents(Customer customer) {
        log.info("Consumer consumes the events {} ", customer.toString());
    }
}
