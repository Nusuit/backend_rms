package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyVerifiedException extends AbstractHttpException {
    public UserAlreadyVerifiedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
