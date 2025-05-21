package io.d4tzz.newrms.controller.auth;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
import io.d4tzz.newrms.dto.auth.ResetAccessTokenResponse;
import io.d4tzz.newrms.service.auth.RecruiterAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/recruiter")
public class RecruiterAuthController {
    private final RecruiterAuthService recruiterAuthService;

    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token-recruiter";

    public RecruiterAuthController(RecruiterAuthService recruiterAuthService) {
        this.recruiterAuthService = recruiterAuthService;
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody RecruiterLoginRequest request, HttpServletResponse httpServletResponse) {
        RecruiterLoginResponse response = recruiterAuthService.login(request);

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, response.getRefreshToken());
        cookie.setPath("/api/auth/recruiter/login/refresh");
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);

        return ApiResponse.success(response);
    }

    @PostMapping("/login/refresh")
    public ApiResponse<?> refreshAccessToken(HttpServletRequest httpServletRequest) {
        String refreshToken = "";

        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                refreshToken = cookie.getValue();
            }
        }

        ResetAccessTokenResponse response = recruiterAuthService.resetAccessToken(refreshToken);

        return ApiResponse.success(response);
    }
}
