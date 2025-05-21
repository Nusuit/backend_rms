package io.d4tzz.newrms.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResetAccessTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
}
