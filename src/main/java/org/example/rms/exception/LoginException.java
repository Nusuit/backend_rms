package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class LoginException extends AbstractHttpStatusException {
    public LoginException() {
        super("Username or password is incorrect");
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(HttpStatus status, String message) {
        super(status, message);
    }
}
