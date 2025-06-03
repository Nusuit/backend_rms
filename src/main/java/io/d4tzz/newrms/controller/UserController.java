package io.d4tzz.newrms.controller;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.candidate.CandidateDto;
import io.d4tzz.newrms.dto.recruiter.RecruiterDto;
import io.d4tzz.newrms.entity.enums.RoleName;
import io.d4tzz.newrms.security.JwtUserPrincipal;
import io.d4tzz.newrms.service.profile.CandidateProfileService;
import io.d4tzz.newrms.service.profile.RecruiterProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final CandidateProfileService candidateProfileService;
    private final RecruiterProfileService recruiterProfileService;

    @GetMapping("/me")
    public ApiResponse<?> getCurrentUserProfile(@AuthenticationPrincipal JwtUserPrincipal userPrincipal) {
        System.out.println("UserController: getCurrentUserProfile called for user ID: " + userPrincipal.getIdentity());

        if (userPrincipal == null) {
            return ApiResponse.builder().success(false).message("User not authenticated").build();
        }

        Long userId = userPrincipal.getIdentity();
        String role = userPrincipal.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse(null);

        System.out.println("UserController: User ID: " + userId + ", Role: " + role);

        if (role == null) {
            return ApiResponse.builder().success(false).message("User role not found").build();
        }

        try {
            if (RoleName.APPLICANT.name().equals(role)) {
                CandidateDto candidateDto = candidateProfileService.getProfile();
                return ApiResponse.success(candidateDto, "Applicant profile fetched successfully");
            } else if (RoleName.RECRUITER.name().equals(role)) {
                RecruiterDto recruiterDto = recruiterProfileService.getProfile(userId);
                // Kiểm tra xem đây có phải là Super Recruiter không
                boolean isSuperRecruiter = false;
                if (recruiterDto != null && "hacnguyet108@gmail.com".equals(recruiterDto.getEmail())) { // Đã đổi getUsername thành getEmail
                    isSuperRecruiter = true;
                }

                Map<String, Object> responsePayload = new java.util.HashMap<>();
                responsePayload.put("user", recruiterDto);
                responsePayload.put("isSuperRecruiter", isSuperRecruiter);

                return ApiResponse.success(responsePayload, "Recruiter profile fetched successfully");
            } else {
                return ApiResponse.builder().success(false).message("Unsupported user role").build();
            }
        } catch (Exception e) {
            System.err.println("UserController: Error fetching user profile: " + e.getMessage());
            e.printStackTrace();
            return ApiResponse.builder().success(false).message("Error fetching user profile: " + e.getMessage()).build();
        }
    }
}
