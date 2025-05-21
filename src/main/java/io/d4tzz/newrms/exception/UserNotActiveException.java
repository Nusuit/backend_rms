package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class UserNotActiveException extends AbstractHttpException {
    public UserNotActiveException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
