package io.d4tzz.newrms.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecruiterSignupResponse {
    private Long id;
    private String email;
    private String role;
    private String name;
}