package io.d4tzz.newrms.exception;

import io.d4tzz.newrms.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handle(Exception e) {
        ApiResponse<?> apiResponse = ApiResponse
                .builder()
                .success(false)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(500).body(apiResponse);
    }

    @ExceptionHandler(AbstractHttpException.class)
    public ResponseEntity<ApiResponse<?>> handle(AbstractHttpException e) {
        ApiResponse<?> apiResponse = ApiResponse
                .builder()
                .success(false)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationErrorDetail> errorDetails = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorDetails.add(new ValidationErrorDetail(error.getField(), error.getDefaultMessage(), null));
        });

        return ApiResponse
                .builder()
                .success(false)
                .message(e.getMessage())
                .errors(errorDetails)
                .build();
    }

    @ExceptionHandler(TooManyOtpRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleException(TooManyOtpRequestException e) {
        ApiResponse<?> apiResponse = ApiResponse
                .builder()
                .success(false)
                .message(e.getMessage())
                .errors(Map.of("waitingTime", e.getWaitingTimeInSeconds()))
                .build();

        return new ResponseEntity<>(apiResponse, e.getHttpStatus());
    }
}
