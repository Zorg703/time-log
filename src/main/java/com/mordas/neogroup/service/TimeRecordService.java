package com.mordas.neogroup.service;

import com.mordas.neogroup.dto.TimeRecordDTO;
import com.mordas.neogroup.entity.TimeRecord;
import com.mordas.neogroup.producer.TimeRecordProducer;
import com.mordas.neogroup.repository.TimeRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeRecordService {
    private static final long RECONNECT_INTERVAL_MS = 5000;
    private static final long MAX_ALLOWED_ELAPSED_TIME_MS = 1000;

    private static final Logger logger = LoggerFactory.getLogger(TimeRecordService.class);

    private final TimeRecordRepository repository;
    private final TimeRecordProducer producer;

    public TimeRecordService(TimeRecordRepository repository, TimeRecordProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public List<TimeRecordDTO> getAll() {
        List<TimeRecord> records = repository.findAll();
        return records.stream()
                .map(record -> new TimeRecordDTO(record.getTime().truncatedTo(ChronoUnit.SECONDS).toString(), record.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(timeout = 5)
    //If transaction will take more then 2 seconds the record will be put in the queue again
    public void save(TimeRecordDTO dto) {
        long startTime = System.currentTimeMillis();

        TimeRecord timeRecord = new TimeRecord(LocalDateTime.parse(dto.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        try {
            TimeRecord record = repository.save(timeRecord);
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > MAX_ALLOWED_ELAPSED_TIME_MS) {
                logger.warn("Elapsed time exceeded max allowed elapsed time {} for record with id {} (ms)", elapsedTime, record.getId());
            }

            logger.info("Record was write successfully : {}", timeRecord);
        } catch (Exception e) {
            logger.error("Cannot connect to database. Will try again in " + RECONNECT_INTERVAL_MS/1000 +  " seconds.");
            try {
                Thread.sleep(RECONNECT_INTERVAL_MS);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            throw e;//RabbitMQ will put the record in the queue
        }
    }

    @Scheduled(fixedRate = 1000)
    public void processTime() {
        producer.sendCurrentTime();
    }
}
