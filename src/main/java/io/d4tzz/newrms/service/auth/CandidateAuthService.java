package io.d4tzz.newrms.service.auth;


import io.d4tzz.newrms.dto.auth.*;

/**
 * Service interface for handling candidate signup operations.
 */

public interface CandidateAuthService {
    CandidateSignupResponse signup(CandidateSignupRequest request);

    void verifyEmail(VerifyEmailRequest request);

    void resendOtp(ResendOtpRequest request);

    CandidateLoginResponse login(CandidateLoginRequest request);

    ResetAccessTokenResponse resetAccessToken(String refreshToken);

    CandidateLoginResponse oAuth2Login(String code);

    void logout(Long userId);
}
