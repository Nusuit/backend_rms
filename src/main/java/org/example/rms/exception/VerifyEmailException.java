package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class VerifyEmailException extends AbstractHttpStatusException {
    public VerifyEmailException(String message) {
        super(message);
    }

    public VerifyEmailException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
