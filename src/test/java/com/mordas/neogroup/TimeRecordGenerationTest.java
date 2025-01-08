package com.mordas.neogroup;

import com.mordas.neogroup.base.BaseIntegrationTest;
import com.mordas.neogroup.entity.TimeRecord;
import com.mordas.neogroup.repository.TimeRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class TimeRecordGenerationTest extends BaseIntegrationTest {
    @Autowired
    private TimeRecordRepository repository;

    @Test
    void testTimeLogGeneration() throws InterruptedException {
        Thread.sleep(1000);

        List<TimeRecord> logs = repository.findAll();

        assertThat(logs).isNotEmpty();
        assertThat(logs.get(0).getTime()).isNotNull();
    }
}
