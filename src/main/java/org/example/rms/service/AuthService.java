package org.example.rms.service;

import jakarta.servlet.http.HttpServletResponse;
import org.example.rms.dto.authentication.*;
import org.example.rms.exception.LoginException;

public interface AuthService {
    LoginResponse login(LoginRequest request, HttpServletResponse httpResponse) throws LoginException;

    SignupResponse signupCandidate(SignupRequest request);

    SignupResponse signupRecruiter(SignupRequest request);

    RefreshResponse refreshToken(String refreshToken);
}
