package com.mordas.neogroup.dto;

public class TimeRecordDTO {
    private Long id;

    private String timestamp;

    public TimeRecordDTO() {
    }

    public TimeRecordDTO(String timestamp) {
        this.timestamp = timestamp;
    }

    public TimeRecordDTO(String timestamp, Long id) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TimeRecordDto{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

