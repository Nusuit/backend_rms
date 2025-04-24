package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class InterviewRecordAlreadyPassedException extends AbstractHttpStatusException {
    public InterviewRecordAlreadyPassedException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
