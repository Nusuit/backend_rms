package org.example.rms.service;

import lombok.RequiredArgsConstructor;
import org.example.rms.dto.recruiter.*;
import org.example.rms.entity.*;
import org.example.rms.exception.ResourceNotFoundException;
import org.example.rms.mapper.recruiter.ApplicationForRecruiterMapper;
import org.example.rms.mapper.recruiter.JobForRecruiterMapper;
import org.example.rms.mapper.recruiter.GroupedSkillMapper;
import org.example.rms.mapper.recruiter.CreateJobMapper;
import org.example.rms.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImp {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final RecruiterRepository recruiterRepository;
    private final SkillOfJobRepository skillOfJobRepository;
    private final ApplicationRepository applicationRepository;

    private final CreateJobMapper createJobMapper;
    private final GroupedSkillMapper groupedSkillMapper;
    private final JobForRecruiterMapper jobForRecruiterMapper;
    private final ApplicationForRecruiterMapper applicationForRecruiterMapper;


    public Page<JobForRecruiterResponse> getJobs(Long recruiterId, Pageable pageable) {
        Page<Job> jobs = jobRepository.findByRecruiterId(recruiterId, pageable);

        return jobs.map(jobForRecruiterMapper::toGetJobResponse);
    }

    public JobForRecruiterResponse getJob(Long recruiterId, Long jobId) {
        Job job = jobRepository.findByIdAndRecruiterId(jobId, recruiterId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );

        return jobForRecruiterMapper.toGetJobResponse(job);
    }

    public Page<ApplicationForRecruiterResponse> getApplicationsOfJob(Long jobId, Long recruiterId, Pageable pageable) {
        Page<Application> applications = applicationRepository.findByJobIdAndJobRecruiterId(jobId, recruiterId, pageable);

        return applications.map(applicationForRecruiterMapper::toApplicationResponse);
    }

    public ApplicationForRecruiterResponse getApplicationOfJob(Long applicationId, Long recruiterId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new ResourceNotFoundException("Application not belong to recruiter");
        }

        return applicationForRecruiterMapper.toApplicationResponse(application);
    }

    public List<GroupedSkillResponse> getSkills() {
        return skillRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Skill::getSkillGroup,
                        Collectors.mapping(groupedSkillMapper::toSkillResponse, Collectors.toList())
                ))
                .entrySet()
                .stream()
                .map(entry -> GroupedSkillResponse
                        .builder()
                        .group(entry.getKey())
                        .skills(entry.getValue())
                        .build()
                )
                .toList();
    }

    public Object postJob(Long recruiterId, CreateJobRequest postJobRequest) {
        Job job = createJobMapper.toJob(postJobRequest);
        job.setRecruiter(recruiterRepository.getReferenceById(recruiterId));
        job.setStatus(JobStatus.OPEN);

        Set<SkillOfJob> skills = job.getSkills();
        job.setSkills(null);

        job = jobRepository.save(job);

        for (SkillOfJob skill : skills) {
            skill.setJob(job);
        }
        skillOfJobRepository.saveAll(skills);

        return null;
    }

    public Object deleteSkillsOfJob(Long recruiterId, Long jobId, List<DeleteSkillOfJobRequest> request) {
        jobRepository.findByIdAndRecruiterId(jobId, recruiterId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found or not owned")
        );

        request.forEach(skill -> {
            skillOfJobRepository.findByJobIdAndSkillId(jobId, skill.getId()).ifPresent(skillOfJobRepository::delete);
        });

        return null;
    }


    public Object updateSkillsOfJob(Long recruiterId, Long jobId, List<UpdateSkillOfJobRequest> request) {
        jobRepository.findByIdAndRecruiterId(jobId, recruiterId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found or not owned")
        );

        request.forEach(skill -> {
            SkillOfJob skillOfJob = skillOfJobRepository.findByJobIdAndSkillId(jobId, skill.getId()).orElse(null);
            if (skillOfJob != null) {
                skillOfJob.setRequired(skill.isRequired());
            } else {
                skillOfJob = new SkillOfJob();
                skillOfJob.setSkill(skillRepository.getReferenceById(skill.getId()));
                skillOfJob.setJob(jobRepository.getReferenceById(jobId));
                skillOfJob.setRequired(skill.isRequired());
            }
            skillOfJobRepository.save(skillOfJob);
        });

        return null;
    }

    public Object noteApplication(Long recruiterId, Long applicationId, NoteApplicationRequest request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new ResourceNotFoundException("Application not belong to recruiter");
        };

        application.setRecruiterNote(request.getRecruiterNote());
        return applicationRepository.save(application);
    }
}
