package com.ipap;

import com.ipap.dto.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Slf4j
public class KafkaConsumerTest {

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));

    @DynamicPropertySource
    public static void initKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.consumer.key-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add("spring.kafka.consumer.value-serializer", () -> "org.springframework.kafka.support.serializer.JsonSerializer");
        registry.add("spring.kafka.consumer.group-id", () -> "tc-demo-group");
        registry.add("spring.kafka.consumer.properties.spring.json.trusted.packages", () -> "com.ipap.dto");
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void testConsumeEvents() {
        log.info("testConsumerEvents method execution stared...");
        Customer customer = new Customer(263, "test user", "test@mail.com", "12345678");
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("testcontainer-demo-topic-2", customer);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info(("Sent message=[" + customer.toString()  +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]"));
            } else {
                log.info(("Unable to send message=["
                        + customer.toString() + "] due to : " + ex.getMessage()));
            }
        });
        log.info("testConsumerEvents method execution ended...");

        await().pollInterval(Duration.ofSeconds(3)).atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            // assert statement

        });
    }
}
