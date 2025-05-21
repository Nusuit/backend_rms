package io.d4tzz.newrms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TooManyOtpRequestException extends AbstractHttpException {
    private long waitingTimeInSeconds;

    public TooManyOtpRequestException(String message, long waitingTimeInSeconds) {
        super(HttpStatus.TOO_MANY_REQUESTS, message);
        this.waitingTimeInSeconds = waitingTimeInSeconds;
    }
}
