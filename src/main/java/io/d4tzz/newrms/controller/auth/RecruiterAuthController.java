// Path: src/main/java/io/d4tzz/newrms/controller/auth/RecruiterAuthController.java
package io.d4tzz.newrms.controller.auth;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
// Import cho RecruiterSignupRequest và RecruiterSignupResponse đã được xóa
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
    private final String REFRESH_TOKEN_COOKIE_PATH = "/api/auth/recruiter/login/refresh";

    public RecruiterAuthController(RecruiterAuthService recruiterAuthService) {
        this.recruiterAuthService = recruiterAuthService;
    }

    // Phương thức signup() đã được XÓA khỏi đây.
    // Việc tạo tài khoản Recruiter giờ đây sẽ do Admin quản lý,
    // ví dụ: thông qua một giao diện quản trị hoặc thêm trực tiếp vào database.

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody RecruiterLoginRequest request, HttpServletResponse httpServletResponse) {
        RecruiterLoginResponse response = recruiterAuthService.login(request);

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, response.getRefreshToken());
        cookie.setPath(REFRESH_TOKEN_COOKIE_PATH);
        cookie.setHttpOnly(true);
        // Cân nhắc thêm:
        // cookie.setSecure(true); // Nếu dùng HTTPS
        // cookie.setMaxAge(thời_gian_sống_bằng_giây);
        httpServletResponse.addCookie(cookie);

        return ApiResponse.success(response, "Login successful");
    }

    @PostMapping("/login/refresh")
    public ApiResponse<?> refreshAccessToken(HttpServletRequest httpServletRequest) {
        String refreshToken = null;

        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Refresh token not found in cookies.")
                    .build();
            // Hoặc có thể throw một exception cụ thể nếu muốn GlobalExceptionHandler xử lý
            // throw new InvalidRequestException("Refresh token not found in cookies.");
        }

        ResetAccessTokenResponse response = recruiterAuthService.resetAccessToken(refreshToken);
        return ApiResponse.success(response);
    }
}
