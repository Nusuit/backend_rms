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
import org.springframework.transaction.annotation.Transactional;

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
    private final SavedJobRepository savedJobRepository;


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

//        JobStage firstJobStage = job.getStages().stream().findFirst().orElseThrow();
//        if (firstJobStage.getOrder() != 1) {
//            throw new RuntimeException("Logic error: job stage order not equal to 1");
//        }

        Stage firstStage = job.getProcess().getStages().stream().filter(stage -> stage.getOrder() == 1).findFirst().orElseThrow();

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
//        interview.setJobStage(firstJobStage);
        interview.setStage(firstStage);

        // Kiểm tra xem có schedule nào cho stage đầu tiên của job này không
        // Nếu không có, có thể cần tạo một schedule mặc định hoặc throw lỗi
        List<Schedule> schedulesForFirstStage = scheduleRepository.findByJobIdAndJobStageOrder(jobId, 1);
        Schedule scheduleOfFirstJobStage = schedulesForFirstStage.stream().findFirst().orElse(null); // Sử dụng .orElse(null) để tránh NoSuchElementException

        if (scheduleOfFirstJobStage == null) {
            // Xử lý trường hợp không tìm thấy schedule cho stage đầu tiên
            // Ví dụ: throw new ResourceNotFoundException("No schedule found for the first stage of this job.");
            // Hoặc tạo một schedule mặc định nếu logic cho phép
            System.err.println("Warning: No schedule found for the first stage of job " + jobId + ". Interview will be created without a schedule.");
        }
        interview.setSchedule(scheduleOfFirstJobStage); // schedule có thể là null nếu không tìm thấy

        interviewRepository.save(interview);

        List<ApplicationStageDto> applicationStageDtos = job.getProcess().getStages()
                .stream()
                .map(stage -> {

                    Interview existingInterview = interviewRepository.findByApplicationIdAndStageId(application.getId(), stage.getId())
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
                            .stageId(stage.getId())
                            .stageName(stage.getName())
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

    @Override
    @Transactional
    public Page<CandidateJobDto> getSavedJobs(Pageable pageable) {
        Long candidateId = getUserIdentity();
        Page<SavedJob> savedJobs = savedJobRepository.findByCandidateId(candidateId, pageable);
        
        return savedJobs.map(savedJob -> {
            Job job = savedJob.getJob();
            boolean applicable = applicationRepository.findByJobIdAndCandidateId(job.getId(), candidateId).isEmpty()
                    && job.getStatus() == JobStatus.OPEN;
            return jobMapper.toCandidateJobDto(job, applicable);
        });
    }

    @Override
    @Transactional 
    public void saveJob(Long jobId) {
        Long candidateId = getUserIdentity();
        
        // Check if already saved
        if (savedJobRepository.findByJobIdAndCandidateId(jobId, candidateId).isPresent()) {
            throw new InvalidRequestException("Job already saved");
        }

        // Get job and candidate
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new ResourceNotFoundException("Candidate not found")
        );

        // Create and save the SavedJob
        SavedJob savedJob = new SavedJob();
        savedJob.setJob(job);
        savedJob.setCandidate(candidate);
        savedJobRepository.save(savedJob);
    }

    @Override
    @Transactional
    public void unsaveJob(Long jobId) {
        Long candidateId = getUserIdentity();
        savedJobRepository.deleteByJobIdAndCandidateId(jobId, candidateId);
    }

    private Specification<Job> createSpecification(JobFilterDto filter) {
        String title = filter.getTitle();
        // String industry = filter.getIndustry(); // Xóa hoặc điều chỉnh nếu Job không còn liên quan trực tiếp đến Industry

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
                // .and( JobSpecification.belongsToIndustry(industry) ) // Xóa hoặc điều chỉnh
                .and( JobSpecification.deadlineFrom(deadlineFrom) )
                .and( JobSpecification.deadlineTo(deadlineTo) )
                .and( JobSpecification.matchesSalaryRange(minSalary, maxSalary) );
    }
}

