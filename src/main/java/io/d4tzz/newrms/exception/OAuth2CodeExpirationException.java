package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class OAuth2CodeExpirationException extends AbstractHttpException {
    public OAuth2CodeExpirationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
