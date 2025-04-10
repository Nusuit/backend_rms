package org.example.rms.controller;


import lombok.RequiredArgsConstructor;
import org.example.rms.dto.ApiResponse;
import org.example.rms.dto.recruiter.*;
import org.example.rms.security.UserPrincipal;
import org.example.rms.service.RecruiterServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class RecruiterController {
    private final RecruiterServiceImp recruiterService;


    @GetMapping("/jobs")
    public ApiResponse<?> getJobs(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Page<JobForRecruiterResponse> jobResponsePage = recruiterService.getJobs(userPrincipal.getIdentity(), pageable);

        return ApiResponse
                .builder()
                .success(true)
                .payload(jobResponsePage)
                .build();
    }

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<?> getJob(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long jobId) {
        JobForRecruiterResponse getJobResponse = recruiterService.getJob(userPrincipal.getIdentity(), jobId);
        return ApiResponse
                .builder()
                .success(true)
                .payload(getJobResponse)
                .build();
    }

    @GetMapping("/jobs/{jobId}/applications")
    public ApiResponse<?> getApplicationsOfJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long jobId, Pageable pageable) {
        Page<ApplicationForRecruiterResponse> applicationResponsePage =
                recruiterService.getApplicationsOfJob(jobId, userPrincipal.getIdentity(), pageable);

        return ApiResponse.successBuild(applicationResponsePage);
    }

    @GetMapping("/applications/{applicationId}")
    public ApiResponse<?> getApplication(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable Long applicationId) {
        ApplicationForRecruiterResponse applicationResponse = recruiterService.getApplicationOfJob(applicationId, userPrincipal.getIdentity());

        return ApiResponse.successBuild(applicationResponse);
    }

    @GetMapping("/skills")
    public ApiResponse<?> getSkills() {
        List<GroupedSkillResponse> groupedSkills = recruiterService.getSkills();

        return ApiResponse.successBuild(groupedSkills);
    }

    @PostMapping("/jobs")
    public ApiResponse<?> postJob(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CreateJobRequest postJobRequest) {
        recruiterService.postJob(userPrincipal.getIdentity(), postJobRequest);

        return ApiResponse.successBuild(null);
    }

    @DeleteMapping("/jobs/{jobId}/skills")
    public ApiResponse<?> deleteSkillsOfJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long jobId, @RequestBody List<DeleteSkillOfJobRequest> request) {
        recruiterService.deleteSkillsOfJob(userPrincipal.getIdentity(), jobId, request);

        return ApiResponse.successBuild(null);
    }

    @PutMapping("/jobs/{jobId}/skills")
    public ApiResponse<?> updateSkillsOfJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long jobId, @RequestBody List<UpdateSkillOfJobRequest> request) {
        recruiterService.updateSkillsOfJob(userPrincipal.getIdentity(), jobId, request);

        return ApiResponse.successBuild(null);
    }

    @PatchMapping("/applications/{applicationId}/note")
    public ApiResponse<?> noteApplication(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long applicationId, @RequestBody NoteApplicationRequest request) {
        recruiterService.noteApplication(userPrincipal.getIdentity(), applicationId, request);

        return ApiResponse.successBuild(null);
    }
}
