package org.example.rms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResendOtpException extends AbstractHttpStatusException {
    private long waitingTimeInSeconds;


    public ResendOtpException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public ResendOtpException(HttpStatus httpStatus, String message, long waitingTimeInSeconds) {
        super(httpStatus, message);
        this.waitingTimeInSeconds = waitingTimeInSeconds;

    }


}
