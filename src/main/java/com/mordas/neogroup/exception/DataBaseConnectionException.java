package com.mordas.neogroup.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DataBaseConnectionException extends RuntimeException{
    private String message;
    public DataBaseConnectionException(String message) {
        super(message);
        this.message = message;
    }
}
