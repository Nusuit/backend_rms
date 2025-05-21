package io.d4tzz.newrms.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class RecruiterLoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
}
