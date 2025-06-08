package io.d4tzz.newrms.controller.job;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.job.*;
import io.d4tzz.newrms.service.job.RecruiterJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
@Slf4j
public class RecruiterJobController {

    private final RecruiterJobService recruiterJobService;

    @PostMapping("/jobs")
    public ApiResponse<RecruiterJobDto> createJob(@Valid @RequestBody CreateJobRequest request) {
        log.info("🔍 [JobController] Creating job with title: {}", request.getTitle());
        RecruiterJobDto jobDto = recruiterJobService.createJob(request);
        log.info("✅ [JobController] Job created successfully with ID: {}", jobDto.getId());
        return ApiResponse.success(jobDto);
    }

    @GetMapping("/jobs")
    public ApiResponse<Page<RecruiterJobDto>> getJobs(@ModelAttribute JobFilterDto filter, Pageable pageable) {
        log.info("🔍 [JobController] Getting jobs with filter: {}, pageable: {}", filter, pageable);
        Page<RecruiterJobDto> jobDtoPage = recruiterJobService.getJobs(filter, pageable);
        log.info("📊 [JobController] Found {} jobs, total elements: {}", 
                jobDtoPage.getContent().size(), jobDtoPage.getTotalElements());
        return ApiResponse.success(jobDtoPage);
    }

    @GetMapping("/skills")
    public ApiResponse<?> getSkills(@RequestParam(required = false) String name, Pageable pageable) {
        return ApiResponse.success(recruiterJobService.getSkills(name, pageable));
    }

    @GetMapping("/processes")
    public ApiResponse<?> getRecruitmentProcesses(@RequestParam(required = false) String name, Pageable pageable) {
        return ApiResponse.success(recruiterJobService.getRecruitmentProcess(name, pageable));
    }

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<RecruiterJobDto> getJobDetail(@PathVariable Long jobId) {
        RecruiterJobDto jobDto = recruiterJobService.getJobDetail(jobId);
        return ApiResponse.success(jobDto);
    }

    @PutMapping("/jobs/{jobId}")
    public ApiResponse<RecruiterJobDto> updateJob(@Valid @RequestBody UpdateJobRequest request, @PathVariable Long jobId) {
        RecruiterJobDto jobDto = recruiterJobService.updateJob(jobId, request);
        return ApiResponse.success(jobDto);
    }

    @PatchMapping("/jobs/{jobId}/cancel")
    public ApiResponse<RecruiterJobDto> cancelJob(@PathVariable Long jobId, @RequestBody CancelJobRequest request) {
        RecruiterJobDto jobDto = recruiterJobService.cancelJob(jobId, request);
        return ApiResponse.success(jobDto);
    }
}


