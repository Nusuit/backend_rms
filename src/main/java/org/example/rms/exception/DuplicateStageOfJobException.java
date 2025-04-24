package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class DuplicateStageOfJobException extends AbstractHttpStatusException {
    public DuplicateStageOfJobException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
