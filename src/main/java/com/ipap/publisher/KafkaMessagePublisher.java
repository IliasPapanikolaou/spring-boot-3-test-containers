package com.ipap.publisher;

import com.ipap.dto.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, Object> template;

    public KafkaMessagePublisher(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    public void sendMessageToTopic(String message) {
        CompletableFuture<SendResult<String, Object>> future =
                template.send("testcontainer-demo-topic-1", message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info(("Sent message=[" + message  + "] with offset=[" + result.getRecordMetadata().offset() + "]"));
            } else {
                log.info(("Unable to send message=[" + message + "] due to : " + ex.getMessage()));
            }
        });
    }

    public void sendEventsToTopic(Customer customer) {
        try {
            CompletableFuture<SendResult<String, Object>> future =
                    template.send("testcontainer-demo-topic-2", customer);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info(("Sent message=[" + customer.toString()  +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]"));
                } else {
                    log.info(("Unable to send message=["
                            + customer.toString() + "] due to : " + ex.getMessage()));
                }
            });
        } catch (Exception ex) {
            log.error("ERROR: " + ex.getMessage());
        }
    }
}
