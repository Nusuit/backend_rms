package org.example.rms;

import org.example.rms.dto.response.ApiResponse;
import org.example.rms.exception.ErrorDetail;
import org.example.rms.exception.LoginException;
import org.example.rms.exception.SignupException;
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
        List<ErrorDetail> errorDetails = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorDetails.add(new ErrorDetail(error.getField(), error.getDefaultMessage(), null));
        });

        return ApiResponse
                .builder()
                    .code(RmsCode.ValidationError.getCode())
                    .success(false)
                    .message(RmsCode.ValidationError.getMessage())
                    .errors(errorDetails)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {

        return null;
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleException(LoginException e) {
        ApiResponse<?> apiResponse = ApiResponse
                .builder()
                    .code(RmsCode.IncorrectUsernameOrPassword.getCode())
                    .success(false)
                    .message("Username or password incorrect ")
                .build();
        return apiResponse;
    }

    @ExceptionHandler(SignupException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> handleException(SignupException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        e.getFieldErrors().forEach((field, error) -> {errorDetails.add(new ErrorDetail(field, error, null));});
        return ApiResponse
                .builder()
                    .code(RmsCode.CanNotCreateUser.getCode())
                    .success(false)
                    .message("Cannot create user")
                    .errors(errorDetails)
                .build();

    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleException(AuthenticationException e) {
        return ApiResponse
                .builder()
                    .code(401)
                    .success(false)
                    .message("Authentication failed")
                .build();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleException(AuthorizationDeniedException e) {
        return ApiResponse
                .builder()
                    .code(403)
                    .success(false)
                    .message("Authorization failed")
                .build();
    }

}
