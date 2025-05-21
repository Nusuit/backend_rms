package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class OtpExpiredException extends AbstractHttpException {
    public OtpExpiredException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
