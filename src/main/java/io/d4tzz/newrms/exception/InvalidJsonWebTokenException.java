package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class InvalidJsonWebTokenException extends AbstractHttpException {
    public InvalidJsonWebTokenException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
