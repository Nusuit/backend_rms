package org.example.rms.security;

import lombok.Getter;

@Getter
public enum RmsCode {
    IncorrectUsernameOrPassword(1000, "Incorrect username or password"),
    CanNotCreateUser(2000, "Can't create user"),
    ValidationError(3000, "Validation error"),
    ;

    private final int code;
    private final String message;

    RmsCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
