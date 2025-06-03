package io.d4tzz.newrms.controller.auth;


import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.auth.*;
import io.d4tzz.newrms.service.auth.CandidateAuthService; // Giữ nguyên tên service
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/applicant") // Đã đổi từ /api/auth/candidate
public class CandidateAuthController {
    private final CandidateAuthService candidateAuthService;

    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";
    private final String REFRESH_TOKEN_COOKIE_PATH = "/api/auth/applicant/login/refresh"; // Đã đổi từ /api/auth/candidate/login/refresh

    @Autowired
    public CandidateAuthController(CandidateAuthService candidateAuthService) {
        this.candidateAuthService = candidateAuthService;
    }

    @PostMapping("/signup")
    ApiResponse<?> candidateSignup(@Valid @RequestBody CandidateSignupRequest request) {
        CandidateSignupResponse response =  candidateAuthService.signup(request);

        return ApiResponse.success(response, "Registration successful! Please verify the email to complete the process");
    }

    @PostMapping("/signup/verify")
    ApiResponse<?> verifyEmail(@RequestBody VerifyEmailRequest request) {
        candidateAuthService.verifyEmail(request);
        return ApiResponse.success();
    }

    @PostMapping("/signup/resend-otp")
    ApiResponse<?> resendOtp(@RequestBody ResendOtpRequest request) {
        candidateAuthService.resendOtp(request);

        return ApiResponse.success(null, "Resend OTP through email successful");
    }

    @PostMapping("/login")
    ApiResponse<?> login(@RequestBody CandidateLoginRequest request, HttpServletResponse httpServletResponse) {
        CandidateLoginResponse response = candidateAuthService.login(request);

        Cookie cookie = createRefreshTokenCookie(response.getRefreshToken());
        httpServletResponse.addCookie(cookie);

        return ApiResponse.success(response, "Login successful");
    }

    @PostMapping("/login/refresh")
    ApiResponse<?> refreshAccessToken(HttpServletRequest httpServletRequest) {
        String refreshToken = "";

        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) { // Thêm kiểm tra null cho cookies
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                    refreshToken = cookie.getValue();
                    break; // Thêm break sau khi tìm thấy cookie
                }
            }
        }


        ResetAccessTokenResponse response = candidateAuthService.resetAccessToken(refreshToken);
        return ApiResponse.success(response);
    }

    @PostMapping("/login/oauth2")
    ApiResponse<?> oAuth2Login(@RequestParam("code") String code, HttpServletResponse httpServletResponse) {
        CandidateLoginResponse response = candidateAuthService.oAuth2Login(code);

        Cookie cookie = createRefreshTokenCookie(response.getRefreshToken());
        httpServletResponse.addCookie(cookie);

        return ApiResponse.success(response);
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        cookie.setHttpOnly(true);
        // Cân nhắc thêm:
        // cookie.setSecure(true); // Nếu dùng HTTPS
        // cookie.setMaxAge(thời_gian_sống_bằng_giây);
        return cookie;
    }
}

