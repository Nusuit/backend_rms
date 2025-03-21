package org.example.rms.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.example.rms.security.RmsCode;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<P> {
    private int code;

    private boolean success;

    private String message;

    private P payload;

    private List<?> errors;

    public static <P> ApiResponse<P> success(P payload, String message) {
        return ApiResponse
                .<P>builder()
                    .code(200)
                    .message(message)
                    .payload(payload)
                .build();
    }
}
