// Path: src/main/java/io/d4tzz/newrms/service/auth/RecruiterAuthService.java
package io.d4tzz.newrms.service.auth;

import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
import io.d4tzz.newrms.dto.auth.RecruiterSignupRequest;
import io.d4tzz.newrms.dto.auth.RecruiterSignupResponse;
import io.d4tzz.newrms.dto.auth.ResetAccessTokenResponse;

public interface RecruiterAuthService {
    RecruiterLoginResponse login(RecruiterLoginRequest request);

    ResetAccessTokenResponse resetAccessToken(String refreshToken);

    RecruiterSignupResponse createRecruiter(RecruiterSignupRequest request);
}
