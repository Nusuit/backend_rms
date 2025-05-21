package io.d4tzz.newrms.service.job;

import io.d4tzz.newrms.dto.job.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruiterJobService {
    Page<RecruiterJobDto> getJobs(JobFilterDto filter, Pageable pageable);

//    List<IndustryDto> getIndustries();

//    SkillsAndStagesByIndustryDto getSkillsAndStagesByIndustry(Long industryId);

    RecruiterJobDto createJob(CreateJobRequest request);

    RecruiterJobDto updateJob(Long jobId, UpdateJobRequest request);

    RecruiterJobDto cancelJob(Long jobId, CancelJobRequest request);
}
