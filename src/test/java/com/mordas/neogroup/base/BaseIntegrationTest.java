package com.mordas.neogroup.base;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.sql.SQLOutput;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Testcontainers
public abstract class BaseIntegrationTest {
    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:4.0.0-alpine")
            .withExposedPorts(5672, 15672);
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgresql = new PostgreSQLContainer("postgres:12.3")
            .withDatabaseName("tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @BeforeAll
    static void startContainers() {
        rabbitmq = new RabbitMQContainer("rabbitmq:4.0.0-alpine")
                .withExposedPorts(5672, 15672);
        rabbitmq.start();
        postgresql.withConnectTimeoutSeconds(30);

        postgresql.start();;
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.stream.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.stream.port", () -> rabbitmq.getMappedPort(5672));
        registry.add("spring.datasource.url", postgresql::getJdbcUrl);
        registry.add("spring.datasource.username", postgresql::getUsername);
        registry.add("spring.datasource.password", postgresql::getPassword);
    }

    public static void shutdownDatabase() throws InterruptedException {
        postgresql.stop();
    }

    public static void startDatabase() throws IOException, InterruptedException {
        postgresql.start();

        postgresql.waitingFor(Wait.forListeningPort());
        System.out.println();
    }
}