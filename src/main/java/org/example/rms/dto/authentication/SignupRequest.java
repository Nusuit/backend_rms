package org.example.rms.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$",
            message = "Password must be at least 8 characters long, contain at uppercase, lowercase and special character."
    )
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
}
