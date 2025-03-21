package org.example.rms.exception;

import java.util.HashMap;
import java.util.Map;

public class SignupException extends RuntimeException {
    private final Map<String, String> fieldErrors = new HashMap<>();

    public SignupException(String message) {
        super(message);
    }

    public SignupException() {}

    public void addError(String fielddName, String fieldValue) {
        fieldErrors.put(fielddName, fieldValue);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public boolean hasErrors() {
        return !fieldErrors.isEmpty();
    }
}
