package io.d4tzz.newrms.service.job;

import io.d4tzz.newrms.dto.application.*;
import io.d4tzz.newrms.dto.job.ApplyJobRequest;
import io.d4tzz.newrms.dto.job.CandidateJobDto;
import io.d4tzz.newrms.dto.job.JobFilterDto;
import io.d4tzz.newrms.entity.*;
import io.d4tzz.newrms.entity.enums.ApplicationStatus;
import io.d4tzz.newrms.entity.enums.InterviewStatus;
import io.d4tzz.newrms.entity.enums.JobStatus;
import io.d4tzz.newrms.exception.InvalidRequestException;
import io.d4tzz.newrms.exception.ResourceNotFoundException;
import io.d4tzz.newrms.mapper.JobMapper;
import io.d4tzz.newrms.repository.*;
import io.d4tzz.newrms.service.AbstractService;
import io.d4tzz.newrms.spec.JobSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateJobServiceImpl extends AbstractService implements CandidateJobService {
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final JobMapper jobMapper;
    private final CandidateRepository candidateRepository;
    private final InterviewRepository interviewRepository;
    private final ScheduleRepository scheduleRepository;


    @Override
    public Page<CandidateJobDto> getJobs(JobFilterDto filter, Pageable pageable) {
        Long candidateId = getUserIdentity();

        Specification<Job> jobSpecification = createSpecification(filter);
        pageable = ensureSortedPageable(pageable);

        Page<Job> jobPage = jobRepository.findAll(jobSpecification, pageable);

        return jobPage.map(job -> {
            boolean applicable =  applicationRepository.findByJobIdAndCandidateId(job.getId(), candidateId).isEmpty()
                    && job.getStatus() == JobStatus.OPEN;
            return jobMapper.toCandidateJobDto(job, applicable);
        });
    }


    @Override
    public ApplicationDto applyJob(Long jobId, ApplyJobRequest request) {
        Long candidateId = getUserIdentity();
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new ResourceNotFoundException("Candidate not found")
        );

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );

        if (job.getStatus() != JobStatus.OPEN) {
            throw new InvalidRequestException("Job is not open for application");
        }

        if (applicationRepository.findByJobIdAndCandidateId(jobId, candidateId).isPresent()) {
            throw new InvalidRequestException("Candidate has already applied to this job");
        }

        JobStage firstJobStage = job.getStages().stream().findFirst().orElseThrow();
        if (firstJobStage.getOrder() != 1) {
            throw new RuntimeException("Logic error: job stage order not equal to 1");
        }


        Application application = new Application();
        application.setJob(job);
        application.setCandidate(candidate);
        application.setStatus(ApplicationStatus.PROGRESS);
        application.setCoverLetter(request.getCoverLetter());
        application.setCvUrl(candidate.getCvUrl());
        applicationRepository.save(application);

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setStatus(InterviewStatus.PROGRESS);
        interview.setJobStage(firstJobStage);

        Schedule scheduleOfFirstJobStage = scheduleRepository.findByJobIdAndJobStageOrder(jobId, 1)
                .stream().findFirst().orElseThrow();
        interview.setSchedule(scheduleOfFirstJobStage);

        interviewRepository.save(interview);

        List<ApplicationStageDto> applicationStageDtos = job.getStages()
                .stream()
                .map(jobStage -> {

                    Interview existingInterview = interviewRepository.findByApplicationIdAndJobStageId(application.getId(), jobStage.getId())
                            .orElse(null);
                    ApplicationInterviewDto applicationInterviewDto = null;
                    if (existingInterview != null) {
                        applicationInterviewDto = ApplicationInterviewDto.builder()
                                .interviewId(existingInterview.getId())
                                .status(existingInterview.getStatus())
                                .build();
                    }

                    Schedule schedule = existingInterview == null ? null : existingInterview.getSchedule();
                    ApplicationScheduleDto applicationScheduleDto = null;
                    if (schedule != null) {
                        applicationScheduleDto = ApplicationScheduleDto.builder()
                                .scheduleId(schedule.getId())
                                .location(schedule.getLocation())
                                .startTime(schedule.getStartTime())
                                .build();
                    }

                    return ApplicationStageDto.builder()
                            .stageId(jobStage.getId())
                            .stageName(jobStage.getStage().getName())
                            .interview(applicationInterviewDto)
                            .schedule(applicationScheduleDto)
                            .build();

                }).toList();

        ApplicationJobDto applicationJobDto = ApplicationJobDto.builder()
                .jobId(job.getId())
                .title(job.getTitle())
                .stages(applicationStageDtos)
                .build();

        return ApplicationDto.builder()
                .id(application.getId())
                .note(application.getNote())
                .cvUrl(application.getCvUrl())
                .coverLetter(application.getCoverLetter())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .updatedAt(application.getUpdatedAt())
                .job(applicationJobDto)
                .build();
    }


    private Specification<Job> createSpecification(JobFilterDto filter) {
        String title = filter.getTitle();
        String industry = filter.getIndustry();

        /* Dam bao deadlineFrom va deadlineTo dong thoi khac null*/
        LocalDate deadlineFrom = filter.getDeadlineFrom();
        LocalDate deadlineTo = filter.getDeadlineTo();
        if ((deadlineFrom == null) != (deadlineTo == null)) {
            throw new InvalidRequestException("Both start and end deadlines are required");
        }

        /* Dam bao minSalary va maxSalary dong thoi khac null*/
        Long minSalary = filter.getMinSalary();
        Long maxSalary = filter.getMaxSalary();
        if ((minSalary == null) != (maxSalary == null)) {
            throw new InvalidRequestException("Both minimum and maximum salary must be provided");
        }

        return JobSpecification.hasTitle(title)
                .and( JobSpecification.belongsToIndustry(industry) )
                .and( JobSpecification.deadlineFrom(deadlineFrom) )
                .and( JobSpecification.deadlineTo(deadlineTo) )
                .and( JobSpecification.matchesSalaryRange(minSalary, maxSalary) );
    }
}
