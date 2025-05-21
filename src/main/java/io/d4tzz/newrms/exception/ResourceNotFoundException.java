package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractHttpException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
