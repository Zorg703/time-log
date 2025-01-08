package com.mordas.neogroup;

import com.mordas.neogroup.base.BaseIntegrationTest;
import com.mordas.neogroup.entity.TimeRecord;
import com.mordas.neogroup.producer.TimeRecordProducer;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class RabbitMQFallbackTest extends BaseIntegrationTest {
    @Autowired
    private TimeRecordProducer producer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testFallbackToRabbitMQ() throws InterruptedException {
        TimeRecord timeLog = new TimeRecord(LocalDateTime.now());


        //Stop db
        BaseIntegrationTest.shutdownDatabase();
        producer.sendCurrentTime();

        Thread.sleep(1000);

        // Check that message was sent
        TimeRecord message = (TimeRecord) rabbitTemplate.receiveAndConvert("time_queue");

        assertThat(message).isNotNull();
        assertThat(message.getTime()).isEqualTo(timeLog.getTime());
    }
}
