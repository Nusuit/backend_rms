package io.d4tzz.newrms.controller.auth;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.auth.UserProfileResponse;
import io.d4tzz.newrms.service.auth.CommonAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class CommonAuthController {
    private final CommonAuthService commonAuthService;

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getCurrentUserProfile() {
        UserProfileResponse response = commonAuthService.getCurrentUserProfile();
        return ApiResponse.success(response);
    }
} 