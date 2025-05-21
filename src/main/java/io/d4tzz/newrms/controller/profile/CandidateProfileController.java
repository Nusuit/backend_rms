package io.d4tzz.newrms.controller.profile;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.candidate.CandidateDto;
import io.d4tzz.newrms.dto.candidate.UpdateCandidateInfoRequest;
import io.d4tzz.newrms.service.profile.CandidateProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    @GetMapping("/profile")
    public ApiResponse<?> getProfile() {
        CandidateDto candidateDto = candidateProfileService.getProfile();

        return ApiResponse.success(candidateDto);
    }

    @PatchMapping("/profile/info")
    public ApiResponse<?> updateInformation(@RequestBody UpdateCandidateInfoRequest request) {
        CandidateDto candidateDto = candidateProfileService.updateInformation(request);

        return ApiResponse.success(candidateDto);
    }

    @PatchMapping("/profile/cv")
    public ApiResponse<?> updateCvFile(@RequestPart("cv") MultipartFile file) {
        CandidateDto candidateDto = candidateProfileService.updateCv(file);

        return ApiResponse.success(candidateDto);
    }

    @PatchMapping("/profile/avatar")
    public ApiResponse<?> updateAvatarFile(@RequestPart("avatar") MultipartFile file) {
        CandidateDto candidateDto = candidateProfileService.updateAvatar(file);

        return ApiResponse.success(candidateDto);
    }
}
