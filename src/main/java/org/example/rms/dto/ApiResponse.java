package org.example.rms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<P> {
    private Integer code;

    private boolean success;

    private String message;

    private P payload;

    private List<?> errors;

    public static <P> ApiResponse<P> successBuild(P payload) {
        return ApiResponse
                .<P>builder()
                .success(true)
                .payload(payload)
                .build();
    }

    public static <P> ApiResponse<P> successBuild() {
        return ApiResponse.successBuild(null);
    }
}
