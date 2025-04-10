package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class DuplicateApplicationException extends AbstractHttpStatusException{
    public DuplicateApplicationException(String message) {
        super(message);
    }

    public DuplicateApplicationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
