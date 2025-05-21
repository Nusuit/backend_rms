package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class PasswordNotMatchedException extends AbstractHttpException {
    public PasswordNotMatchedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
