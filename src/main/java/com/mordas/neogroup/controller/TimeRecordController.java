package com.mordas.neogroup.controller;

import com.mordas.neogroup.dto.TimeRecordDTO;
import com.mordas.neogroup.exception.DataBaseConnectionException;
import com.mordas.neogroup.exception.ErrorResponse;
import com.mordas.neogroup.service.TimeRecordService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeRecordController {
    private final TimeRecordService service;

    public TimeRecordController(TimeRecordService service) {
        this.service = service;
    }

    @GetMapping("/time")
    public List<TimeRecordDTO> getTime() {
        try {
            return service.getAll();
        } catch (Exception e) {
            throw new DataBaseConnectionException("Database connection error");
        }
    }

    @ExceptionHandler(DataBaseConnectionException.class)
    public ErrorResponse handleException(DataBaseConnectionException e) {
        return new ErrorResponse(e.getMessage());
    }
}