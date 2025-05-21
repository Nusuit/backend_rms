package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends AbstractHttpException {
    public EmailAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
