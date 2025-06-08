package io.d4tzz.newrms.controller.job;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.application.ApplicationDto;
import io.d4tzz.newrms.dto.job.ApplyJobRequest;
import io.d4tzz.newrms.dto.job.CandidateJobDto;
import io.d4tzz.newrms.dto.job.JobFilterDto;
import io.d4tzz.newrms.service.job.CandidateJobServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applicant")
@RequiredArgsConstructor
public class CandidateJobController {
    private final CandidateJobServiceImpl candidateJobService;

    @GetMapping("/jobs")
    public ApiResponse<Page<CandidateJobDto>> getJobs(@ModelAttribute JobFilterDto filter, Pageable pageable) {
        Page<CandidateJobDto> jobDtoPage = candidateJobService.getJobs(filter, pageable);
        return ApiResponse.success(jobDtoPage);
    }

    @PostMapping("/jobs/{jobId}")
    public ApiResponse<ApplicationDto> applyJob(@PathVariable Long jobId, @RequestBody ApplyJobRequest applyJobRequest) {
        ApplicationDto applicationDto = candidateJobService.applyJob(jobId, applyJobRequest);
        return ApiResponse.success(applicationDto);
    }

    @GetMapping("/saved-jobs")
    public ApiResponse<Page<CandidateJobDto>> getSavedJobs(Pageable pageable) {
        Page<CandidateJobDto> savedJobs = candidateJobService.getSavedJobs(pageable);
        return ApiResponse.success(savedJobs);
    }

    @PostMapping("/saved-jobs/{jobId}")
    public ApiResponse<Void> saveJob(@PathVariable Long jobId) {
        candidateJobService.saveJob(jobId);
        return ApiResponse.success(null, "Job saved successfully");
    }

    @DeleteMapping("/saved-jobs/{jobId}") 
    public ApiResponse<Void> unsaveJob(@PathVariable Long jobId) {
        candidateJobService.unsaveJob(jobId);
        return ApiResponse.success(null, "Job unsaved successfully");
    }
}

