package org.example.rms.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class SignupException extends AbstractHttpStatusException {
    public SignupException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }


}
