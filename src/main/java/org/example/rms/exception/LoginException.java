package org.example.rms.exception;

public class LoginException extends RuntimeException {
    public LoginException() {
        super("Username or password is incorrect");
    }
    public LoginException(String message) {
        super(message);
    }
}
