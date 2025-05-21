package io.d4tzz.newrms.service.job;

import io.d4tzz.newrms.dto.job.*;
import io.d4tzz.newrms.entity.*;
import io.d4tzz.newrms.entity.enums.ApplicationStatus;
import io.d4tzz.newrms.entity.enums.JobStatus;
import io.d4tzz.newrms.exception.*;
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
import java.util.HashSet;
import java.util.LinkedHashSet;
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
    private final IndustryRepository industryRepository;
    private final JobStageRepository jobStageRepository;
    private final SkillRepository skillRepository;
    private final StageRepository stageRepository;
    private final ScheduleRepository scheduleRepository;


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

//    public List<IndustryDto> getIndustries() {
//        List<Industry> industries = industryRepository.findAll();
//        return industries.stream()
//                .map(industry -> IndustryDto.builder()
//                        .id(industry.getId())
//                        .name(industry.getName())
//                        .build()
//                )
//                .toList();
//    }

//    public SkillsAndStagesByIndustryDto getSkillsAndStagesByIndustry(Long industryId) {
//        industryRepository.findById(industryId).orElseThrow(
//                () -> new ResourceNotFoundException("Industry not found")
//        );
//
//        List<Skill> skills = skillRepository.findByIndustryId(industryId);
//        List<SkillByIndustryDto> skillDtos = skills.stream()
//                .map(skill -> SkillByIndustryDto.builder()
//                        .id(skill.getId())
//                        .name(skill.getName())
//                        .build()
//                )
//                .toList();
//
//        List<Stage> stages = stageRepository.findByIndustryId(industryId);
//        List<StageByIndustryDto> stageDtos = stages.stream()
//                .map(stage -> StageByIndustryDto.builder()
//                        .id(stage.getId())
//                        .name(stage.getName())
//                        .order(stage.getOrder())
//                        .skipped(false)
//                        .build()
//                )
//                .toList();
//
//        return SkillsAndStagesByIndustryDto.builder().skills(skillDtos).stages(stageDtos).build();
//    }

    @Override
    @Transactional
    public RecruiterJobDto createJob(CreateJobRequest request) {
        Long recruiterId = getUserIdentity();

        /*
         * Validate the request
         */
        CreateJobRequest.StageRequirement firstStageRequirement = request.getStages().stream().findFirst().orElseThrow(
                () -> new InvalidRequestException("Creating job must have at least one stage")
        );

        Stage firstStage = stageRepository.findById(firstStageRequirement.getStageId()).orElseThrow(
                () -> new ResourceNotFoundException("Stage not found")
        );

        if (request.getMinSalary() > request.getMaxSalary()) {
            throw new InvalidRequestException("Starting salary cannot be greater than ending salary");
        }

        /* Create new job */
        Job job = jobMapper.toJob(request);
        job.setRecruiter( recruiterRepository.getReferenceById(recruiterId) );
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

        /*
         * Maps StageRequirement to the JobStage of the created job.
         */
        Set<JobStage> jobStages = new LinkedHashSet<>(request.getStages().size());
        int order = 1;
        for (CreateJobRequest.StageRequirement stageRequirement : request.getStages()) {
            Long stageId = stageRequirement.getStageId();

            JobStage jobStage = new JobStage();
            jobStage.setJob(job);
            Stage stage = stageRepository.findById(stageId).orElseThrow(() -> new ResourceNotFoundException("Stage not found"));
            jobStage.setStage(stage);
            jobStage.setOrder(order++);

            jobStages.add(jobStage);
        }
        jobStageRepository.saveAll(jobStages);

        job.setSkills(jobSkills);
        job.setStages(jobStages);

        /*
         * Create the schedule for first stage (default stage)
         */
        Schedule schedule = new Schedule();
        schedule.setJob(job);
        schedule.setJobStage(jobStageRepository.findByJobIdAndOrder(job.getId(), 1).orElseThrow());
        schedule.setName("CV Screening");
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

        return JobSpecification.hasRecruiterId(recruiterId)
                .and( JobSpecification.hasTitle(title) )
                .and( JobSpecification.belongsToIndustry(industry) )
                .and( JobSpecification.deadlineFrom(deadlineFrom) )
                .and( JobSpecification.deadlineTo(deadlineTo) )
                .and( JobSpecification.matchesSalaryRange(minSalary, maxSalary) );

    }
}
