package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class JobCancelledException extends AbstractHttpException {
    public JobCancelledException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}