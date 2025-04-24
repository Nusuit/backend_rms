package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class OAuth2CodeExpireException extends AbstractHttpStatusException {
    public OAuth2CodeExpireException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
