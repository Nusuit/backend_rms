package io.d4tzz.newrms.controller.job;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.application.ApplicationDto;
import io.d4tzz.newrms.dto.job.ApplyJobRequest;
import io.d4tzz.newrms.dto.job.CandidateJobDto;
import io.d4tzz.newrms.dto.job.JobFilterDto;
import io.d4tzz.newrms.service.job.CandidateJobServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applicant") // Đã đổi từ /api/candidate
public class CandidateJobController {
    private final CandidateJobServiceImpl candidateJobServiceImpl;

    public CandidateJobController(CandidateJobServiceImpl candidateJobServiceImpl) {
        this.candidateJobServiceImpl = candidateJobServiceImpl;
    }

    @GetMapping("/jobs")
    public ApiResponse<?> getJobs(@ModelAttribute JobFilterDto filter, Pageable pageable) {
        Page<CandidateJobDto> jobDtoPage = candidateJobServiceImpl.getJobs(filter, pageable);
        return ApiResponse.success(jobDtoPage);
    }

    @PostMapping("/jobs/{jobId}")
    public ApiResponse<?> applyJob(@PathVariable Long jobId, @RequestBody ApplyJobRequest applyJobRequest) {
        ApplicationDto applicationDto = candidateJobServiceImpl.applyJob(jobId, applyJobRequest);
        return ApiResponse.success(applicationDto);
    }

    @GetMapping("/saved-jobs")
    public ApiResponse<?> getSavedJobs(Pageable pageable) {
        Page<CandidateJobDto> savedJobs = candidateJobServiceImpl.getSavedJobs(pageable);
        return ApiResponse.success(savedJobs);
    }

    @PostMapping("/saved-jobs/{jobId}")
    public ApiResponse<?> saveJob(@PathVariable Long jobId) {
        candidateJobServiceImpl.saveJob(jobId);
        return ApiResponse.success(null, "Job saved successfully");
    }

    @DeleteMapping("/saved-jobs/{jobId}") 
    public ApiResponse<?> unsaveJob(@PathVariable Long jobId) {
        candidateJobServiceImpl.unsaveJob(jobId);
        return ApiResponse.success(null, "Job unsaved successfully");
    }
}

