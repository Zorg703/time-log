package com.mordas.neogroup.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

import static com.mordas.neogroup.configuration.RabbitMQConfiguration.QUEUE_NAME;

@Service
public class TimeRecordProducer {
    private static final Logger logger = Logger.getLogger(TimeRecordProducer.class.getName());
    private final RabbitTemplate rabbitTemplate;

    public TimeRecordProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCurrentTime() {
        String message = LocalDateTime.now().toString();
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        logger.info("Message was sent: " + message);
    }
}
