package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenException extends AbstractHttpStatusException {
    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
