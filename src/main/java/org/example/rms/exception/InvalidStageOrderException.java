package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class InvalidStageOrderException extends AbstractHttpStatusException {
    public InvalidStageOrderException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
