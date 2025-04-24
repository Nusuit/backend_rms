package org.example.rms.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.rms.dto.ApiResponse;
import org.example.rms.dto.recruiter.*;
import org.example.rms.entity.*;
import org.example.rms.exception.DuplicateStageOfJobException;
import org.example.rms.exception.DuplicationSkillOfJobException;
import org.example.rms.exception.InterviewRecordAlreadyPassedException;
import org.example.rms.exception.ResourceNotFoundException;
import org.example.rms.mapper.recruiter.*;
import org.example.rms.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImp {

    private final EmailService emailService;

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final RecruiterRepository recruiterRepository;
    private final SkillOfJobRepository skillOfJobRepository;
    private final StageOfJobRepository stageOfJobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewStageRepository interviewStageRepository;
    private final InterviewRecordRepository interviewRecordRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;

    private final CreateJobMapper createJobMapper;
    private final GroupedSkillMapper groupedSkillMapper;
    private final RecruiterJobMapper recruiterJobMapper;
    private final InterviewRecordMapper interviewRecordMapper;
    private final UpdateJobRequestMapper updateJobRequestMapper;
    private final InterviewScheduleMapper interviewScheduleMapper;
    private final RecruiterApplicationMapper recruiterApplicationMapper;
    private final CreateInterviewScheduleMapper createInterviewScheduleMapper;
    private final NoteApplicationMapper noteApplicationMapper;


    public Page<RecruiterJobResponse> getJobs(Long recruiterId, Pageable pageable) {
        Page<Job> jobs = jobRepository.findByRecruiterId(recruiterId, pageable);

        return jobs.map(recruiterJobMapper::toRecruiterJobResponse);
    }

    public RecruiterJobResponse getJob(Long recruiterId, Long jobId) {
        Job job = jobRepository.findByIdAndRecruiterId(jobId, recruiterId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );

        return recruiterJobMapper.toRecruiterJobResponse(job);
    }

    public List<ApplicationStatus> getApplicationStatuses() {
        return List.of(ApplicationStatus.values());
    }

    public Page<RecruiterApplicationResponse> getApplicationsOfJob(Long jobId, Long recruiterId,
                                                                   List<ApplicationStatus> statuses, Pageable pageable) {
        if (statuses == null) {
            statuses = List.of(ApplicationStatus.values());
        }

        Page<Application> applications =
                applicationRepository.findByJobIdAndJobRecruiterIdAndStatusIn(jobId, recruiterId, statuses, pageable);

        return applications.map(recruiterApplicationMapper::toRecruiterApplicationResponse);
    }

    public RecruiterApplicationResponse getApplicationOfJob(Long applicationId, Long recruiterId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(
                () -> new ResourceNotFoundException("Application not found")
        );

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new ResourceNotFoundException("Application not belong to recruiter");
        }

        return recruiterApplicationMapper.toRecruiterApplicationResponse(application);
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

    public List<InterviewStageResponse> getInterviewStages() {
        List<InterviewStageResponse> response = new ArrayList<>();

        interviewStageRepository.findAll().forEach(interviewStage -> {
            InterviewStageResponse interviewStageResponse = InterviewStageResponse
                    .builder()
                        .id(interviewStage.getId())
                        .stageName(interviewStage.getStageName())
                    .build();
            response.add(interviewStageResponse);
        });

        return response;
    }

    @Transactional
    public CreateJobResponse createJob(Long recruiterId, CreateJobRequest postJobRequest) {
        Job job = createJobMapper.toJob(postJobRequest);
        job.setRecruiter(recruiterRepository.getReferenceById(recruiterId));
        job.setStatus(JobStatus.OPEN);

        Set<SkillOfJob> skills = job.getSkills();
        job.setSkills(null);

        Set<StageOfJob> stages = job.getStages();
        job.setStages(null);

        job = jobRepository.save(job);

        int expectedOrder = 1;
        Set<Long> stageIds = new HashSet<>();
        for (StageOfJob stage : stages) {
            if (!stageIds.contains(stage.getStage().getId())) {
                stageIds.add(stage.getStage().getId());
                stage.setStageOrder(expectedOrder++);
                stage.setJob(job);
            } else {
                throw new DuplicateStageOfJobException("Duplicate stage of job");
            }
        }
        stages = new HashSet<>(stageOfJobRepository.saveAll(stages));
        job.setStages(stages);

        Set<Long> skillIds = new HashSet<>();
        for (SkillOfJob skill : skills) {
            if (!skillIds.contains(skill.getSkill().getId())) {
                skillIds.add(skill.getSkill().getId());
                skill.setJob(job);
            } else {
                throw new DuplicationSkillOfJobException("Duplicate skill of job");
            }

        }
        skills = new HashSet<>(skillOfJobRepository.saveAll(skills));
        job.setSkills(skills);

        return createJobMapper.toCreateJobResponse(job);
    }

    public UpdateJobResponse updateJob(Long recruiterId, Long jobId, UpdateJobRequest request) {
        Job job = jobRepository.findByIdAndRecruiterId(jobId, recruiterId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found or not owned")
        );

        updateJobRequestMapper.updateJob(request, job);

        jobRepository.save(job);

        return updateJobRequestMapper.toUpdateJobResponse(job);
    }

    @Transactional
    public void isQualifiedApplication(Long recruiterId, Long jobId, Long applicationId, boolean isQualified) {
        assertJobBelongToRecruiter(recruiterId, jobId);
        Application application = assertApplicationBelongToJob(jobId, applicationId);

        if (isQualified) {
            application.setStatus(ApplicationStatus.INTERVIEW_QUALIFIED);

            InterviewRecord interviewRecord = new InterviewRecord();
            interviewRecord.setApplication(application);
            StageOfJob stageOfJob = stageOfJobRepository.findByJobIdAndStageOrder(jobId, 1).orElseThrow();
            interviewRecord.setStage(stageOfJob.getStage());
            interviewRecordRepository.save(interviewRecord);

            // Gui email thong bao

        } else {
            application.setStatus(ApplicationStatus.INTERVIEW_UNQUALIFIED);

            // Gui email thong bao
        }

    }

    public CreateInterviewScheduleResponse createInterviewSchedule(Long recruiterId, Long jobId, Long stageId,
                                                                   CreateInterviewScheduleRequest request) {
        Job job = assertJobBelongToRecruiter(recruiterId, jobId);

        StageOfJob stageOfJob = stageOfJobRepository.findByJobIdAndStageId(jobId, stageId).orElseThrow(
                () -> new ResourceNotFoundException("Job or Stage not found")
        );

        InterviewStage stage = stageOfJob.getStage();

        System.out.println(request.getTime());

        InterviewSchedule interviewSchedule = createInterviewScheduleMapper.toInterviewSchedule(request);
        interviewSchedule.setStage(stage);
        interviewSchedule.setJob(job);


        interviewSchedule = interviewScheduleRepository.save(interviewSchedule);

        return createInterviewScheduleMapper.toCreateInterviewScheduleResponse(interviewSchedule);
    }

    public Page<InterviewScheduleResponse> getInterviewSchedules(Long recruiterId, Long jobId, Long stageId, Pageable pageable) {
        assertJobBelongToRecruiter(recruiterId, jobId);
        assertStageBelongToJob(jobId, stageId);

        Page<InterviewSchedule> interviewSchedules = interviewScheduleRepository.findByJobIdAndStageId(jobId, stageId, pageable);

        return interviewSchedules.map(interviewScheduleMapper::toInterviewScheduleResponse);
    }

    public Page<InterviewRecordResponse> getInterviewRecordsForSchedule(Long recruiterId, Long jobId, Long stageId,
                                                                        Long scheduleId, Pageable pageable) {
        assertJobBelongToRecruiter(recruiterId, jobId);
        assertStageBelongToJob(jobId, stageId);
        assertScheduleBelongToStage(stageId, scheduleId);

        Page<InterviewRecord> interviewRecords =
                interviewRecordRepository.findByApplicationJobIdAndStageIdAndScheduleIsNull(jobId, stageId, pageable);

        return interviewRecords.map(interviewRecordMapper::toInterviewRecordResponse);
    }

    @Transactional
    public List<InterviewRecordResponse> assignInterviewRecordToSchedule(Long recruiterId, Long jobId,
                                                                         Long stageId, Long scheduleId,
                                                                         List<AssignedInterviewRecord> request) {
        assertJobBelongToRecruiter(recruiterId, jobId);
        assertStageBelongToJob(jobId, stageId);
        InterviewSchedule schedule = assertScheduleBelongToStage(stageId, scheduleId);

        List<InterviewRecordResponse> response = new ArrayList<>();

        request.forEach(assignedInterviewRecord -> {
            InterviewRecord interviewRecord = interviewRecordRepository.findByIdAndApplicationJobIdAndStageIdAndScheduleIsNull(
                            assignedInterviewRecord.getId(), jobId, stageId
            ).orElseThrow(() -> new ResourceNotFoundException("Interview record not found"));

            interviewRecord.setSchedule(schedule);
            interviewRecordRepository.save(interviewRecord);

            response.add(interviewRecordMapper.toInterviewRecordResponse(interviewRecord));
        });

        return response;
    }

    public Page<InterviewRecordResponse> getInterviewRecordsOfSchedule(Long recruiterId, Long jobId, Long stageId, Long scheduleId, Pageable pageable) {
        InterviewSchedule schedule = interviewScheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ResourceNotFoundException("Schedule not found")
        );

        Page<InterviewRecord> interviewRecords = interviewRecordRepository.findByScheduleId(scheduleId, pageable);

        return interviewRecords.map(interviewRecordMapper::toInterviewRecordResponse);
    }

    public NoteInterviewRecordResponse noteInterviewRecord(Long recruiterId, Long jobId, Long stageId, Long scheduleId,
                                      Long interviewRecordId, NoteInterviewRecordRequest request) {
        assertJobBelongToRecruiter(recruiterId, jobId);
        assertStageBelongToJob(jobId, stageId);
        assertScheduleBelongToStage(stageId, scheduleId);
        InterviewRecord interviewRecord = assertRecordBelongToSchedule(interviewRecordId, scheduleId);

        interviewRecord.setNote(request.getNote());
        interviewRecordRepository.save(interviewRecord);

        return NoteInterviewRecordResponse.builder().note(interviewRecord.getNote()).build();
    }

    @Transactional
    public List<NextInterviewStageResponse> moveToNextStage(Long recruiterId, Long jobId, Long stageId, Long scheduleId,
                                  List<NextInterviewStageRequest> request) throws MessagingException {
        assertJobBelongToRecruiter(recruiterId, jobId);
        assertStageBelongToJob(jobId, stageId);
        assertScheduleBelongToStage(stageId, scheduleId);


        StageOfJob previousStage = stageOfJobRepository.findByStageIdAndJobId(stageId, jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found")
        );

        StageOfJob nextStage = stageOfJobRepository.findByJobIdAndStageOrder(jobId, previousStage.getStageOrder() + 1).orElse(null);

        boolean isFinalStage = nextStage == null;

        List<NextInterviewStageResponse> response = new ArrayList<>();

        if (!isFinalStage) {
            for (NextInterviewStageRequest interviewRecord : request) {
                InterviewRecord passedRecord = interviewRecordRepository.findByIdAndScheduleId(interviewRecord.getId(), scheduleId).orElseThrow(
                        () -> new ResourceNotFoundException("Interview record " + interviewRecord.getId() +" not found")
                );

                if (passedRecord.isPassed()) {
                    throw new InterviewRecordAlreadyPassedException("Interview record " + passedRecord.getId() +" already passed");
                }

                passedRecord.setPassed(true);
                interviewRecordRepository.save(passedRecord);

                InterviewRecord nextStageRecord = new InterviewRecord();
                nextStageRecord.setStage(nextStage.getStage());
                nextStageRecord.setApplication(passedRecord.getApplication());
                interviewRecordRepository.save(nextStageRecord);


                response.add(NextInterviewStageResponse.builder().id(passedRecord.getId()).build());

                // Gui email thong bao

            }
        } else {
            for (NextInterviewStageRequest interviewRecord : request) {
                // Gui email thong bao

                InterviewRecord finalRecord = interviewRecordRepository.findByIdAndScheduleId(interviewRecord.getId(), scheduleId).orElseThrow(
                        () -> new ResourceNotFoundException("Interview record " + interviewRecord.getId() +" not found")
                );

                finalRecord.setPassed(true);
                finalRecord.getApplication().setStatus(ApplicationStatus.ACCEPTED);
            }
        }

        return response;
    }

    @Transactional
    public List<UpdateSkillOfJobResponse> updateSkillsOfJob(Long recruiterId, Long jobId, List<UpdateSkillOfJobRequest> request) {
        jobRepository.findByIdAndRecruiterId(jobId, recruiterId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found or not owned")
        );

        skillOfJobRepository.deleteByJobId(jobId);

        List<UpdateSkillOfJobResponse> response = new ArrayList<>();
        Job job = jobRepository.getReferenceById(jobId);

        Set<Long> skillIds = new HashSet<>();
        for (UpdateSkillOfJobRequest skill : request) {
            if (skillIds.contains(skill.getId())) {
                throw new DuplicationSkillOfJobException("Duplicate skill of job");
            }
            skillIds.add(skill.getId());

            SkillOfJob skillOfJob = new SkillOfJob();
            skillOfJob.setJob(job);
            skillOfJob.setSkill(skillRepository.getReferenceById(skill.getId()));
            skillOfJob.setRequired(skill.isRequired());
            skillOfJobRepository.save(skillOfJob);

            UpdateSkillOfJobResponse skillOfJobResponse = UpdateSkillOfJobResponse
                    .builder()
                    .id(skillOfJob.getSkill().getId())
                    .required(skillOfJob.isRequired())
                    .skillName(skillOfJob.getSkill().getSkillName())
                    .build();
            response.add(skillOfJobResponse);
        }

        return response;
    }

    @Transactional
    public List<UpdateStageOfJobResponse> updateStagesOfJob(Long recruiterId, Long jobId, List<UpdateStageOfJobRequest> request) {
        jobRepository.findByIdAndRecruiterId(jobId, recruiterId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found or not owned")
        );

        stageOfJobRepository.deleteByJobId(jobId);

        List<UpdateStageOfJobResponse> response = new ArrayList<>();
        Job job = jobRepository.getReferenceById(jobId);;

        int expectedOrder = 1;
        Set<Long> stageIds = new HashSet<>();
        for (UpdateStageOfJobRequest stage : request) {
            if (stageIds.contains(stage.getId())) {
                throw new DuplicateStageOfJobException("Duplicate stage of job");
            }
            stageIds.add(stage.getId());

            StageOfJob stageOfJob = new StageOfJob();
            stageOfJob.setJob(job);
            stageOfJob.setStage(interviewStageRepository.getReferenceById(stage.getId()));
            stageOfJob.setStageOrder(expectedOrder++);
            stageOfJobRepository.save(stageOfJob);

            UpdateStageOfJobResponse stageOfJobResponse = UpdateStageOfJobResponse
                    .builder()
                    .id(stageOfJob.getStage().getId())
                    .stageName(stageOfJob.getStage().getStageName())
                    .stageOrder(stageOfJob.getStageOrder())
                    .build();
            response.add(stageOfJobResponse);
        }

        return response;
    }

    public void deleteJob(Long recruiterId, Long jobId) {
        Job job = assertJobBelongToRecruiter(recruiterId, jobId);

        job.getApplications().forEach(application -> {
            if (application.getStatus() == ApplicationStatus.INTERVIEW_UNQUALIFIED ||
                    application.getStatus() == ApplicationStatus.REJECTED) {
                return;
            }

            // gui email thong bao
        });
        jobRepository.delete(job);
    }

    public NoteApplicationResponse noteApplication(Long recruiterId, Long jobId, Long applicationId, NoteApplicationRequest request) {
        assertJobBelongToRecruiter(recruiterId, jobId);
        Application application = assertApplicationBelongToJob(jobId, applicationId);

        application.setRecruiterNote(request.getRecruiterNote());
        application = applicationRepository.save(application);

        return noteApplicationMapper.toNoteApplicationResponse(application);
    }

    public void setApplicationStatus(Long recruiterId, Long applicationId, ApplicationStatus status) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() ->
                new ResourceNotFoundException("Application not found")
        );

        if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
            throw new ResourceNotFoundException("Application not belong to recruiter");
        }

        application.setStatus(status);
        applicationRepository.save(application);
    }

    private Job assertJobBelongToRecruiter(Long recruiterId, Long jobId) {
        return jobRepository.findByIdAndRecruiterId(jobId, recruiterId).orElseThrow(
                () -> new ResourceNotFoundException("Job not found or not owned")
        );
    }

    private Application assertApplicationBelongToJob(Long jobId, Long applicationId) {
        return applicationRepository.findByIdAndJobId(applicationId, jobId).orElseThrow(
                () -> new ResourceNotFoundException("Application not found or not owned")
        );
    }

    private StageOfJob assertStageBelongToJob(Long jobId, Long stageId) {
        return stageOfJobRepository.findByStageIdAndJobId(stageId, jobId).orElseThrow(
                () -> new ResourceNotFoundException("Stage not found or not owned")
        );
    }

    private InterviewSchedule assertScheduleBelongToStage(Long stageId, Long scheduleId) {
        return interviewScheduleRepository.findByIdAndStageId(scheduleId, stageId).orElseThrow(
                () -> new ResourceNotFoundException("Schedule not found or not owned")
        );
    }

    private InterviewRecord assertRecordBelongToSchedule(Long scheduleId, Long interviewRecordId) {
        return interviewRecordRepository.findByIdAndScheduleId(interviewRecordId, scheduleId).orElseThrow(
                () -> new ResourceNotFoundException("Schedule not found or not owned")
        );
    }

}
