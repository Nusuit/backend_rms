// Path: src/main/java/io/d4tzz/newrms/service/auth/RecruiterAuthService.java
package io.d4tzz.newrms.service.auth;

import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
// Import cho RecruiterSignupRequest và RecruiterSignupResponse đã được xóa
import io.d4tzz.newrms.dto.auth.ResetAccessTokenResponse;

public interface RecruiterAuthService {
    RecruiterLoginResponse login(RecruiterLoginRequest request);

    ResetAccessTokenResponse resetAccessToken(String refreshToken);

    // Phương thức signup(RecruiterSignupRequest request) đã được XÓA khỏi đây.
}
