package io.d4tzz.newrms.service.job;

import io.d4tzz.newrms.dto.application.ApplicationDto;
import io.d4tzz.newrms.dto.job.ApplyJobRequest;
import io.d4tzz.newrms.dto.job.CandidateJobDto;
import io.d4tzz.newrms.dto.job.JobFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateJobService {
    Page<CandidateJobDto> getJobs(JobFilterDto filter, Pageable pageable);

    ApplicationDto applyJob(Long jobId, ApplyJobRequest request);
}

