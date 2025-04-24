package org.example.rms;

import org.example.rms.dto.authentication.ResendOtpResponse;
import org.example.rms.dto.ApiResponse;
import org.example.rms.exception.*;
import org.example.rms.security.RmsCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ValidationErrorDetail> errorDetails = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorDetails.add(new ValidationErrorDetail(error.getField(), error.getDefaultMessage(), null));
        });

        return ApiResponse
                .builder()
                    .success(false)
                    .message(RmsCode.ValidationError.getMessage())
                    .errors(errorDetails)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        System.out.println(e.getMessage());
        return null;
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiResponse<?>> handleException(LoginException e) {
        ApiResponse<?> apiResponse = ApiResponse
                .builder()
                    .success(false)
                    .message("Username or password incorrect")
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<ApiResponse<?>> handleException(SignupException e) {
        ApiResponse<?> a = ApiResponse
                    .builder()
                        .success(false)
                        .message(e.getMessage())
                    .build();

        return ResponseEntity.status(e.getHttpStatus()).body(a);
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleException(AuthenticationException e) {
        return ApiResponse
                .builder()
                    .success(false)
                    .message("Authentication failed")
                .build();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleException(AuthorizationDeniedException e) {
        return ApiResponse
                .builder()
                    .success(false)
                    .message("Authorization failed")
                .build();
    }

    @ExceptionHandler(ResendOtpException.class)
    public ResponseEntity<ApiResponse<?>> handleException(ResendOtpException e) {
        ApiResponse<?> a = ApiResponse
                .builder()
                    .success(false)
                    .message(e.getMessage())
                    .payload(e.getWaitingTimeInSeconds() == 0 ? null : new ResendOtpResponse(e.getWaitingTimeInSeconds()))
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(a);
    }

    @ExceptionHandler(VerifyEmailException.class)
    public ResponseEntity<ApiResponse<?>> handleException(VerifyEmailException e) {
        ApiResponse a = ApiResponse
                .builder()
                    .success(false)
                    .message(e.getMessage())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(a);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleException(RefreshTokenException e) {
        ApiResponse a = ApiResponse
                .builder()
                    .success(false)
                    .message(e.getMessage())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(a);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleException(ResourceNotFoundException e) {
        ApiResponse a = ApiResponse
                .builder()
                    .success(false)
                    .message(e.getMessage())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(a);
    }

    @ExceptionHandler(InterviewRecordAlreadyPassedException.class)
    public ResponseEntity<ApiResponse<?>> handleException(InterviewRecordAlreadyPassedException e) {
        ApiResponse a = ApiResponse
                .builder()
                .success(false)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(a);
    }

    @ExceptionHandler(AbstractHttpStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleException(AbstractHttpStatusException e) {
        ApiResponse a = ApiResponse
                .builder()
                .success(false)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(e.getHttpStatus()).body(a);
    }
}
