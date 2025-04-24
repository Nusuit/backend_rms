package org.example.rms.exception;

import org.springframework.http.HttpStatus;

public class DuplicationSkillOfJobException extends AbstractHttpStatusException {
    public DuplicationSkillOfJobException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
