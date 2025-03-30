package org.example.rms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AbstractHttpStatusException extends RuntimeException {
    protected HttpStatus httpStatus;

    public AbstractHttpStatusException(String message) {
        super(message);
    }

    public AbstractHttpStatusException(HttpStatus httpStatus, String message) {
      super(message);
      this.httpStatus = httpStatus;
    }
}
