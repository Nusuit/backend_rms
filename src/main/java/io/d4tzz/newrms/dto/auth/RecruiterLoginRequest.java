package io.d4tzz.newrms.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email; // Thêm import này
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecruiterLoginRequest {
    @NotBlank(message = "Email must not be blank") // Đã đổi Username thành Email
    @Email(message = "Invalid email format") // Thêm validation email
    private String email; // Đã đổi từ username thành email

    @NotBlank(message = "Password must not be blank")
    private String password;
}
