package com.mordas.neogroup.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    public static final String QUEUE_NAME = "time_queue";

    @Bean
    public Queue timeQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}
