package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends AbstractHttpException {
    public InvalidRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
