package io.d4tzz.newrms.service.job;

import io.d4tzz.newrms.dto.job.*;
import io.d4tzz.newrms.dto.process.RecruitmentProcessDto;
import io.d4tzz.newrms.dto.skill.SkillDto;
import io.d4tzz.newrms.dto.stage.StageDto;
import io.d4tzz.newrms.entity.*;
import io.d4tzz.newrms.entity.enums.ApplicationStatus;
import io.d4tzz.newrms.entity.enums.JobStatus;
import io.d4tzz.newrms.exception.*;
import io.d4tzz.newrms.mapper.JobMapper;
import io.d4tzz.newrms.repository.*;
import io.d4tzz.newrms.service.AbstractService;
import io.d4tzz.newrms.spec.JobSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecruiterJobServiceImpl extends AbstractService implements RecruiterJobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final JobSkillRepository jobSkillRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobStageRepository jobStageRepository;
    private final SkillRepository skillRepository;
    private final ScheduleRepository scheduleRepository;
    private final RecruitmentProcessRepository recruitmentProcessRepository;


    @Override
    @Transactional
    public Page<RecruiterJobDto> getJobs(JobFilterDto filter, Pageable pageable) {
        Long recruiterId = getUserIdentity();

        Specification<Job> jobSpecification = createSpecification(recruiterId, filter);
        pageable = ensureSortedPageable(pageable);

        Page<Job> jobPage = jobRepository.findAll(jobSpecification, pageable);

        return  jobPage.map(job -> {
            long applicationQuantity = applicationRepository.countByJobId(job.getId());
            return jobMapper.toRecruiterJobDto(job, applicationQuantity);
        });
    }

    public Page<SkillDto> getSkills(String skillName, Pageable pageable) {
        Page<Skill> skills;
        if (skillName == null || skillName.isBlank()) {
            skills = skillRepository.findAll(pageable);
        } else {
            skills = skillRepository.findAllByNameContainingIgnoreCase(skillName, pageable);
        }
        return skills.map(skill -> SkillDto.builder().id(skill.getId()).name(skill.getName()).build());
    }

    public Page<RecruitmentProcessDto> getRecruitmentProcess(String processName, Pageable pageable) {
        Page<RecruitmentProcess> recruitmentProcesses;
        if (processName == null || processName.isBlank()) {
            recruitmentProcesses = recruitmentProcessRepository.findAll(pageable);
        } else {
            recruitmentProcesses = recruitmentProcessRepository.findAllByNameContainingIgnoreCase(processName, pageable);
        }

        return recruitmentProcesses.map(process -> {

            List<StageDto> stageDtos = process.getStages()
                    .stream()
                    .map(stage ->
                            StageDto.builder()
                                    .id(stage.getId())
                                    .name(stage.getName())
                                    .order(stage.getOrder())
                                    .build()
                    ).toList();

            return RecruitmentProcessDto.builder()
                    .id(process.getId())
                    .name(process.getName())
                    .stages(stageDtos)
                    .build();
        });
    }

    @Override
    @Transactional
    public RecruiterJobDto createJob(CreateJobRequest request) {
        Long recruiterId = getUserIdentity();

        /*
         * Validate salary
         */
        if (request.getMinSalary() > request.getMaxSalary()) {
            throw new InvalidRequestException("Starting salary cannot be greater than ending salary");
        }

        /*
         * Validate processId
         */
        Long processId = request.getProcessId();
        RecruitmentProcess process = recruitmentProcessRepository.findById(processId).orElseThrow(
                () -> new ResourceNotFoundException("Process not found")
        );

        /*
         * Create new job
         */
        Job job = jobMapper.toJob(request);
        job.setRecruiter( recruiterRepository.getReferenceById(recruiterId));
        job.setProcess(process);
        job = jobRepository.save(job);

        /*
         * Mapping SkillRequirement to JobSkill of the created job
         */
        Set<JobSkill> jobSkills = new HashSet<>(request.getSkills().size());
        for (CreateJobRequest.SkillRequirement skillRequirement : request.getSkills()) {
            Long skillId = skillRequirement.getSkillId();
            boolean required = skillRequirement.isRequired();

            JobSkill jobSkill = new JobSkill();
            jobSkill.setJob(job);
            Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
            jobSkill.setSkill(skill);
            jobSkill.setRequired(required);

            jobSkills.add(jobSkill);
        }
        jobSkillRepository.saveAll(jobSkills);


        Set<JobStage> jobStages = new HashSet<>(process.getStages().size());
        Set<Stage> stages = process.getStages();
        for (Stage stage : stages) {
            JobStage jobStage = new JobStage();
            jobStage.setJob(job);
            jobStage.setStage(stage);
            jobStage.setOrder(stage.getOrder());

            jobStages.add(jobStage);
        }
        jobStageRepository.saveAll(jobStages);

        job.setSkills(jobSkills);
        job.setStages(jobStages);

        /*
         * Create the schedule for first stage (default stage)
         */
        Stage firstStage = stages.stream().filter(stage -> stage.getOrder() == 1).findFirst().orElseThrow();
        Schedule schedule = new Schedule();
        schedule.setJob(job);
        schedule.setJobStage(jobStageRepository.findByJobIdAndOrder(job.getId(), 1).orElseThrow());
        schedule.setStage(firstStage);
        schedule.setName(firstStage.getName());
        scheduleRepository.save(schedule);

        return jobMapper.toRecruiterJobDto(job, 0L);
    }


    @Override
    @Transactional
    public RecruiterJobDto updateJob(Long jobId, UpdateJobRequest request) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );
        if (job.getStatus() == JobStatus.CANCELED) {
            throw new JobCancelledException("Cannot update canceled job");
        }

        jobMapper.updatePartial(request, job);

        job.getSkills().clear();
        jobRepository.save(job);


//        jobSkillRepository.deleteAll(skills); /* Khong biet tai sao khong chay duoc */
        jobSkillRepository.deleteByJobId(jobId);

        Set<JobSkill> jobSkills = new HashSet<>(request.getSkills().size());
        for (UpdateJobRequest.JobSkillRequirement jobSkillRequirement : request.getSkills()) {
            Long skillId = jobSkillRequirement.getSkillId();
            boolean required = jobSkillRequirement.isRequired();

            Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

            JobSkill jobSkill = new JobSkill();
            jobSkill.setJob(job);
            jobSkill.setSkill(skill);
            jobSkill.setRequired(required);

            jobSkills.add(jobSkill);
        }
        jobSkillRepository.saveAll(jobSkills);

        job.setSkills(jobSkills);

        return jobMapper.toRecruiterJobDto(job, applicationRepository.countByJobId(jobId));
    }


    @Override
    @Transactional
    public RecruiterJobDto cancelJob(Long jobId, CancelJobRequest request) {
        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );

        if (job.getStatus() != JobStatus.OPEN) {
            throw new InvalidRequestException("Job is not open for cancellation");
        }

        job.setStatus(JobStatus.CANCELED);
        job.setNote(request.getReason());

        List<Application> applications = applicationRepository.findByJobId(jobId);
        for (Application application : applications) {
            if (application.getStatus() != ApplicationStatus.PROGRESS) continue;

            application.setStatus(ApplicationStatus.JOB_CANCELLED);
            /* Gui email thong bao toi ung vien */

            applicationRepository.save(application);
        }

        job = jobRepository.save(job);

        return jobMapper.toRecruiterJobDto(job, applicationRepository.countByJobId(jobId));
    }


    private Specification<Job> createSpecification(Long recruiterId, JobFilterDto filter) {
        String title = filter.getTitle();
        // String industry = filter.getIndustry(); // Dòng này không còn cần thiết

        /*
         * Ensure both deadlineFrom and deadlineTo are not null
         */
        LocalDate deadlineFrom = filter.getDeadlineFrom();
        LocalDate deadlineTo = filter.getDeadlineTo();
        if ((deadlineFrom == null) != (deadlineTo == null)) {
            throw new InvalidRequestException("Both start and end deadlines are required");
        }


        /*
         * Ensure both minSalary and maxSalary are not null
         */
        Long minSalary = filter.getMinSalary();
        Long maxSalary = filter.getMaxSalary();
        if ((minSalary == null) != (maxSalary == null)) {
            throw new InvalidRequestException("Both minimum and maximum salary must be provided");
        }

        return JobSpecification.hasRecruiterId(recruiterId)
                .and( JobSpecification.hasTitle(title) )
                // .and( JobSpecification.belongsToIndustry(industry) ) // Dòng này đã gây lỗi và cần được loại bỏ
                .and( JobSpecification.deadlineFrom(deadlineFrom) )
                .and( JobSpecification.deadlineTo(deadlineTo) )
                .and( JobSpecification.matchesSalaryRange(minSalary, maxSalary) );

    }
}
