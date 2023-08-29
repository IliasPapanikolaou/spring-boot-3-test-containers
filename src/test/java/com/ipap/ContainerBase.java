package com.ipap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public abstract class ContainerBase {

    @Container
    static final GenericContainer<?> mongoDBContainer = new GenericContainer<>(DockerImageName.parse("mongo:6.0.3"))
            .withExposedPorts(27017)
            .waitingFor(Wait.forLogMessage(".*Waiting for connections.*\\n", 1))
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "datmt_root")
            .withReuse(true)
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "datmt_root");

    static {
        Startables.deepStart(mongoDBContainer).join();
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "investmentDB");
        registry.add("spring.data.mongodb.username", () -> "datmt_root");
        registry.add("spring.data.mongodb.password", () -> "datmt_root");
    }

    @BeforeAll
    public static void beforeAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        mongoDBContainer.stop();
    }
}
