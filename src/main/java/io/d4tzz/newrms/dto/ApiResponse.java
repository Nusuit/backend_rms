package io.d4tzz.newrms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer code;

    private boolean success;

    private String message;

    private T payload;

    private T errors;

    public static <T> ApiResponse<T> success(T payload) {
        return ApiResponse
                .<T>builder()
                .success(true)
                .payload(payload)
                .build();
    }

    public static <T> ApiResponse<T> success(T payload, String message) {
        return ApiResponse
                .<T>builder()
                .success(true)
                .message(message)
                .payload(payload)
                .build();
    }

    public static <T> ApiResponse<T> success() {
        return ApiResponse.success(null);
    }
}
