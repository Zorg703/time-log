package com.mordas.neogroup.consumer;

import com.mordas.neogroup.dto.TimeRecordDTO;
import com.mordas.neogroup.service.TimeRecordService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class TimeRecordConsumer {
    private final TimeRecordService service;

    public TimeRecordConsumer(TimeRecordService service) {
        this.service = service;
    }


    @RabbitListener(queues = "time_queue")
    public void consumeMessage(String message) {
        service.save(new TimeRecordDTO(message));
    }
}
