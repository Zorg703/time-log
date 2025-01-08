package com.mordas.neogroup;

import com.mordas.neogroup.base.BaseIntegrationTest;
import com.mordas.neogroup.consumer.TimeRecordConsumer;
import com.mordas.neogroup.entity.TimeRecord;
import com.mordas.neogroup.repository.TimeRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseRecoveryTest extends BaseIntegrationTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TimeRecordRepository repository;

    @Autowired
    private TimeRecordConsumer consumer;

    @Test
    @Transactional
    void testDatabaseRecoveryAndOrder() throws InterruptedException, IOException {
        BaseIntegrationTest.shutdownDatabase();
        Thread.sleep(5000);
       BaseIntegrationTest.startDatabase();
        Thread.sleep(1000);
        List<TimeRecord> records = repository.findAll();
        LocalDateTime previousTime = null;
        for (TimeRecord record : records) {
            //Check current and previous timestamp
            if (previousTime != null && record.getTime().isBefore(previousTime)) {
                assertThat(false).isTrue();
            }
            //Check value between 2 records
            if (previousTime != null && ChronoUnit.SECONDS.between(previousTime.truncatedTo(ChronoUnit.SECONDS), record.getTime().truncatedTo(ChronoUnit.SECONDS)) != 1) {
                assertThat(false).isTrue();
            }

            previousTime = record.getTime();
        }
    }}
