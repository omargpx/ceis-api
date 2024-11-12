package com.citse.ceis.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class GUSException extends RuntimeException{
    private String Origin;
    private Object data;
    private HttpStatus status;

    public GUSException(String origin, Object data, HttpStatus status) {
        Origin = origin;
        this.data = data;
        this.status = status;
    }
}
