package org.example.rms.dto.authentication;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private long id;
    private String email;
    private String role;
}
