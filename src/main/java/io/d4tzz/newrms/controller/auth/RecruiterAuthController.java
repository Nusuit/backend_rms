// Path: src/main/java/io/d4tzz/newrms/controller/auth/RecruiterAuthController.java
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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/recruiter")
@RequiredArgsConstructor
public class RecruiterAuthController {
    private final RecruiterAuthService recruiterAuthService;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token-recruiter";
    private static final String REFRESH_TOKEN_COOKIE_PATH = "/api/auth/recruiter/login/refresh";

    @PostMapping("/login")
    public ApiResponse<RecruiterLoginResponse> login(@Valid @RequestBody RecruiterLoginRequest request, HttpServletResponse httpServletResponse) {
        RecruiterLoginResponse response = recruiterAuthService.login(request);

        Cookie cookie = createRefreshTokenCookie(response.getRefreshToken());
        httpServletResponse.addCookie(cookie);

        return ApiResponse.success(response, "Login successful");
    }

    @PostMapping("/login/refresh")
    public ApiResponse<ResetAccessTokenResponse> refreshAccessToken(HttpServletRequest httpServletRequest) {
        String refreshToken = extractRefreshTokenFromCookies(httpServletRequest.getCookies());

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ApiResponse.<ResetAccessTokenResponse>builder()
                    .success(false)
                    .message("Refresh token not found in cookies.")
                    .build();
        }

        ResetAccessTokenResponse response = recruiterAuthService.resetAccessToken(refreshToken);
        return ApiResponse.success(response);
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private String extractRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
