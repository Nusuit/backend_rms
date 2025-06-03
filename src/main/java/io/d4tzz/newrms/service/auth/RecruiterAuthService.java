// Path: src/main/java/io/d4tzz/newrms/service/auth/RecruiterAuthService.java
package io.d4tzz.newrms.service.auth;

import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
import io.d4tzz.newrms.dto.auth.RecruiterSignupRequest; // Thêm import này
import io.d4tzz.newrms.dto.auth.RecruiterSignupResponse; // Thêm import này
import io.d4tzz.newrms.dto.auth.ResetAccessTokenResponse;

public interface RecruiterAuthService {
    RecruiterLoginResponse login(RecruiterLoginRequest request);

    ResetAccessTokenResponse resetAccessToken(String refreshToken);

    // Thêm phương thức để tạo tài khoản Recruiter mới (chức năng admin cũ)
    RecruiterSignupResponse createRecruiter(RecruiterSignupRequest request);
}

