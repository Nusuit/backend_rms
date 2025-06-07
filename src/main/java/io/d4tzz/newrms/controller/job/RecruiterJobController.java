package io.d4tzz.newrms.controller.job;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.job.*;
import io.d4tzz.newrms.service.job.RecruiterJobService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterJobController {

    private final RecruiterJobService recruiterJobService;

    public RecruiterJobController(RecruiterJobService recruiterJobService) {
        this.recruiterJobService = recruiterJobService;
    }

    @GetMapping("/test")
    public void test() {

    }

    @PostMapping("/jobs")
    public ApiResponse<?> createJob(@Valid @RequestBody CreateJobRequest request) {
        RecruiterJobDto jobDto = recruiterJobService.createJob(request);
        return ApiResponse.success(jobDto);
    }

    @GetMapping("/jobs")
    public ApiResponse<?> getJobs(@ModelAttribute JobFilterDto filter, Pageable pageable) {
        Page<RecruiterJobDto> jobDtoPage = recruiterJobService.getJobs(filter, pageable);

        return ApiResponse.success(jobDtoPage);
    }

    @GetMapping("/skills")
    public ApiResponse<?> getIndustries(@RequestParam(required = false) String name, Pageable pageable) {
        return ApiResponse.success(recruiterJobService.getSkills(name, pageable));
    }

    @GetMapping("/processes")
    public ApiResponse<?> getSkillsAndStagesByIndustry(@RequestParam(required = false) String name, Pageable pageable) {
        return ApiResponse.success(recruiterJobService.getRecruitmentProcess(name, pageable));
    }

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<?> getJobDetail(@PathVariable Long jobId) {
        RecruiterJobDto jobDto = recruiterJobService.getJobDetail(jobId);
        return ApiResponse.success(jobDto);
    }

    @PutMapping("/jobs/{jobId}")
    public ApiResponse<?> updateJob(@Valid @RequestBody UpdateJobRequest request, @PathVariable Long jobId) {
        RecruiterJobDto jobDto = recruiterJobService.updateJob(jobId, request);

        return ApiResponse.success(jobDto);
    }

    @PatchMapping("jobs/{jobId}/cancel")
    public ApiResponse<?> cancelJob(@PathVariable Long jobId, @RequestBody CancelJobRequest request) {
        RecruiterJobDto jobDto = recruiterJobService.cancelJob(jobId, request);

        return ApiResponse.success(jobDto);
    }
}


