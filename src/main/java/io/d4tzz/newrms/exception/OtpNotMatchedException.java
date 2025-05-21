package io.d4tzz.newrms.exception;

import org.springframework.http.HttpStatus;

public class OtpNotMatchedException extends AbstractHttpException {
    public OtpNotMatchedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
