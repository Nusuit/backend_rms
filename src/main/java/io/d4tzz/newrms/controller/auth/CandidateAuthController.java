package io.d4tzz.newrms.controller.auth;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.auth.*;
import io.d4tzz.newrms.service.auth.CandidateAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/applicant")
@RequiredArgsConstructor
public class CandidateAuthController {
    private final CandidateAuthService candidateAuthService;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";
    private static final String REFRESH_TOKEN_COOKIE_PATH = "/api/auth/applicant/login/refresh";

    @PostMapping("/signup")
    public ApiResponse<CandidateSignupResponse> candidateSignup(@Valid @RequestBody CandidateSignupRequest request) {
        CandidateSignupResponse response = candidateAuthService.signup(request);
        return ApiResponse.success(response, "Registration successful! Please verify the email to complete the process");
    }

    @PostMapping("/signup/verify")
    public ApiResponse<Void> verifyEmail(@RequestBody VerifyEmailRequest request) {
        candidateAuthService.verifyEmail(request);
        return ApiResponse.success();
    }

    @PostMapping("/signup/resend-otp")
    public ApiResponse<Void> resendOtp(@RequestBody ResendOtpRequest request) {
        candidateAuthService.resendOtp(request);
        return ApiResponse.success(null, "Resend OTP through email successful");
    }

    @PostMapping("/login")
    public ApiResponse<CandidateLoginResponse> login(@RequestBody CandidateLoginRequest request, HttpServletResponse httpServletResponse) {
        CandidateLoginResponse response = candidateAuthService.login(request);

        Cookie cookie = createRefreshTokenCookie(response.getRefreshToken());
        httpServletResponse.addCookie(cookie);

        return ApiResponse.success(response, "Login successful");
    }

    @PostMapping("/login/refresh")
    public ApiResponse<ResetAccessTokenResponse> refreshAccessToken(HttpServletRequest httpServletRequest) {
        String refreshToken = "";

        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        ResetAccessTokenResponse response = candidateAuthService.resetAccessToken(refreshToken);
        return ApiResponse.success(response);
    }

    @PostMapping("/login/oauth2")
    public ApiResponse<CandidateLoginResponse> oAuth2Login(@RequestParam("code") String code, HttpServletResponse httpServletResponse) {
        CandidateLoginResponse response = candidateAuthService.oAuth2Login(code);

        Cookie cookie = createRefreshTokenCookie(response.getRefreshToken());
        httpServletResponse.addCookie(cookie);

        return ApiResponse.success(response);
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        cookie.setHttpOnly(true);
        return cookie;
    }
}

