package org.example.rms.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "email cannot be blank")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "password cannot be blank")
    private String password;

}
