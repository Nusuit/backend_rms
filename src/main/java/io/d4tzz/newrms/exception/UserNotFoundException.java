package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AbstractHttpException {
    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
