package io.d4tzz.newrms.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class VerifyEmailRequest {
    @NotBlank(message = "Email must not blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Otp must be blank")
    private String otp;
}