package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class OperationException extends AbstractHttpStatusException {
    public OperationException(String message) {
        super(message);
    }

    public OperationException(HttpStatus status, String message) {
        super(status, message);
    }
}
