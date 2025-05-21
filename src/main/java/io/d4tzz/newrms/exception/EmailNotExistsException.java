package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class EmailNotExistsException extends AbstractHttpException {
    public EmailNotExistsException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
