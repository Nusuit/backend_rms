package io.d4tzz.newrms.service.job;

import io.d4tzz.newrms.dto.job.*;
import io.d4tzz.newrms.dto.process.RecruitmentProcessDto;
import io.d4tzz.newrms.dto.skill.SkillDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruiterJobService {
    Page<RecruiterJobDto> getJobs(JobFilterDto filter, Pageable pageable);

    Page<SkillDto> getSkills(String skillName, Pageable pageable);

    Page<RecruitmentProcessDto> getRecruitmentProcess(String processName, Pageable pageable);

    RecruiterJobDto createJob(CreateJobRequest request);

    RecruiterJobDto updateJob(Long jobId, UpdateJobRequest request);

    RecruiterJobDto cancelJob(Long jobId, CancelJobRequest request);
}
