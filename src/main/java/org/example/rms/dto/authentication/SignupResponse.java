package org.example.rms.dto.authentication;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignupResponse {
    private String email;
    private String role;
}
