package org.example.rms.controller;


import lombok.RequiredArgsConstructor;
import org.example.rms.dto.ApiResponse;
import org.example.rms.dto.candidate.*;
import org.example.rms.security.UserPrincipal;
import org.example.rms.service.CandidateServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateServiceImp candidateService;

    @GetMapping("/jobs")
    public ApiResponse<?> getJobs(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Page<JobForCandidateResponse> jobResponsePage = candidateService.getJobs(userPrincipal.getIdentity(),pageable);

        return ApiResponse.successBuild(jobResponsePage);
    }

    @PostMapping("/jobs/{jobId}/applications")
    public ApiResponse<?> applyJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                   @PathVariable Long jobId, @RequestBody ApplyJobRequest request) {
        ApplyJobResponse response = candidateService.applyJob(userPrincipal.getIdentity(), jobId, request);

        return ApiResponse.successBuild(response);
    }

    @GetMapping("/applications/{applicationId}")
    public ApiResponse<?> getApplication(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long applicationId) {
        ApplicationForCandidateResponse response = candidateService.getApplication(userPrincipal.getIdentity(), applicationId);

        return ApiResponse.successBuild(response);
    }

    @GetMapping("/applications")
    public ApiResponse<?> getApplications(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Page<ApplicationForCandidateResponse> response = candidateService.getApplications(userPrincipal.getIdentity(), Pageable.unpaged());

        return ApiResponse.successBuild(response);
    }

    @PutMapping("/applications/{applicationId}")
    public ApiResponse<?> modifyApplication(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long applicationId, @RequestBody ModifyApplicationRequest request) {
        candidateService.modifyApplication(userPrincipal.getIdentity(), applicationId, request);

        return ApiResponse.successBuild(null);
    }

    @DeleteMapping("/applications/{applicationId}")
    public ApiResponse<?> deleteApplication(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long applicationId) {
        candidateService.deleteApplication(userPrincipal.getIdentity(), applicationId);

        return ApiResponse.successBuild(null);
    }

    @GetMapping("/genders")
    public ApiResponse<?> getGenders() {
        return ApiResponse.successBuild(candidateService.getGenders());
    }

    @GetMapping("/profile")
    public ApiResponse<?> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CandidateProfileResponse response = candidateService.getProfile(userPrincipal.getIdentity());

        return ApiResponse.successBuild(response);
    }

    @PutMapping("/profile")
    public ApiResponse<?> updateProfile(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @RequestBody UpdateCandidateProfileRequest request) {
        UpdateCandidateProfileResponse response = candidateService.updateProfile(userPrincipal.getIdentity(), request);

        return ApiResponse.successBuild(response);
    }

    @PostMapping("/profile-cv")
    public ApiResponse<?> updateCv(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                   @RequestParam("cv") MultipartFile cvFile) {
        UpdateCvResponse response = candidateService.updateCv(userPrincipal.getIdentity(), cvFile);

        return ApiResponse.successBuild(response);
    }

    @PostMapping("/profile-picture")
    public ApiResponse<?> updateProfilePicture(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                   @RequestParam("img") MultipartFile imgFile) {
        UpdateProfilePictureResponse response = candidateService.updateProfilePicture(userPrincipal.getIdentity(), imgFile);

        return ApiResponse.successBuild(response);
    }
}
