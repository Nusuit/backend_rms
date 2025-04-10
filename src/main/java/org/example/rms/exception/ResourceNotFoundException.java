package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractHttpStatusException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND ,message);
    }

}
