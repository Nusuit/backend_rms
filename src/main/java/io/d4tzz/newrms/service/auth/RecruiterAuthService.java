package io.d4tzz.newrms.service.auth;


import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
import io.d4tzz.newrms.dto.auth.ResetAccessTokenResponse;

public interface RecruiterAuthService {
    RecruiterLoginResponse login(RecruiterLoginRequest request);

    ResetAccessTokenResponse resetAccessToken(String refreshToken);
}
