package io.d4tzz.newrms.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CandidateSignupRequest {
    @NotBlank(message = "Email must not blank")
    private String email;

    @NotBlank(message = "Password must not blank")
    private String password;
}
