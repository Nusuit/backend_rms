package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyVerifiedException extends AbstractHttpException {
    public EmailAlreadyVerifiedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
