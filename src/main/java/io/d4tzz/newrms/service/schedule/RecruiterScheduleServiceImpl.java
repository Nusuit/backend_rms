package io.d4tzz.newrms.service.schedule;

import io.d4tzz.newrms.dto.interview.InterviewDto;
import io.d4tzz.newrms.dto.schedule.CreateScheduleRequest;
import io.d4tzz.newrms.dto.schedule.ScheduleDto;
import io.d4tzz.newrms.dto.schedule.UpdateScheduleRequest;
import io.d4tzz.newrms.entity.*;
import io.d4tzz.newrms.entity.enums.ApplicationStatus;
import io.d4tzz.newrms.entity.enums.InterviewStatus;
import io.d4tzz.newrms.entity.enums.JobStatus;
import io.d4tzz.newrms.exception.InvalidRequestException;
import io.d4tzz.newrms.exception.ResourceNotFoundException;
import io.d4tzz.newrms.mapper.InterviewMapper;
import io.d4tzz.newrms.mapper.ScheduleMapper;
import io.d4tzz.newrms.repository.*;
import io.d4tzz.newrms.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruiterScheduleServiceImpl extends AbstractService {
    private final ScheduleMapper scheduleMapper;
    private final JobRepository jobRepository;
    private final JobStageRepository jobStageRepository;
    private final ScheduleRepository scheduleRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewMapper interviewMapper;
    private final ApplicationRepository applicationRepository;
    private final StageRepository stageRepository;


    @Transactional
    public ScheduleDto createSchedule(Long jobId, Long stageId, CreateScheduleRequest request) {
//        JobStage jobStage = jobStageRepository.findByJobIdAndId(jobId, stageId).orElseThrow(
//                () -> new ResourceNotFoundException("job and stage not match")
//        );

        Job job = jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("job not found")
        );

        if (job.getStatus() != JobStatus.OPEN) {
            throw new InvalidRequestException("Job is not open for scheduling");
        }

        if (!job.getRecruiter().getId().equals(getUserIdentity())) {
            throw new InvalidRequestException("Only recruiter own the job can create schedule");
        }

        Stage stage = stageRepository.findById(stageId).orElseThrow(
                () -> new ResourceNotFoundException("stage not found")
        );

        if (!job.getProcess().getStages().contains(stage)) {
            throw new InvalidRequestException("stage not found in recruitment process of job");
        }

        if (stage.getOrder() == 1) {
            throw new InvalidRequestException("First stage cannot be scheduled");
        }

        Schedule schedule = scheduleMapper.toSchedule(request);
        schedule.setJob(job);
//        schedule.setJobStage(jobStage);
        schedule.setStage(stage);

        scheduleRepository.save(schedule);

        return scheduleMapper.toScheduleDto(schedule);
    }

    @Transactional
    public Page<ScheduleDto> getSchedules(Long jobId, Long stageId, Pageable pageable) {
        pageable = ensureSortedPageable(pageable);
//        Page<Schedule> schedulePage = scheduleRepository.findByJobIdAndJobStageId(jobId, jobStage, pageable);
        Page<Schedule> schedulePage = scheduleRepository.findByJobIdAndStageId(jobId, stageId, pageable);
        return schedulePage.map(scheduleMapper::toScheduleDto);
    }

    @Transactional
    public ScheduleDto updateSchedule(Long jobId, Long stageId, Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = fetchSchedule(jobId, stageId, scheduleId);
        if (schedule.getJob().getStatus() != JobStatus.OPEN) {
            throw new InvalidRequestException("Job is not open for updating schedule");
        }
        if (schedule.getJobStage().getOrder() == 1) {
            throw new InvalidRequestException("First stage cannot be updated");
        }

        scheduleMapper.updatePartial(request, schedule);
        scheduleRepository.save(schedule);

        return scheduleMapper.toScheduleDto(schedule);
    }

    @Transactional
    public Page<InterviewDto> getInterviewsForSchedule(Long jobId, Long jobStage, Long scheduleId, Pageable pageable) {
        fetchSchedule(jobId, jobStage, scheduleId);

        Page<Interview> interviewPage = interviewRepository.findByStageIdAndApplicationJobIdAndScheduleIsNull(jobStage, jobId, pageable);

        return interviewPage.map(interviewMapper::toInterviewDto);
    }

    @Transactional
    public void assignInterviewsToSchedule(Long jobId, Long stageId, Long scheduleId, Long interviewId) {
        Schedule schedule = fetchSchedule(jobId, stageId, scheduleId);
        if (schedule.getJob().getStatus() != JobStatus.OPEN) {
            throw new InvalidRequestException("Job is not open for assigning interviews");
        }
        if (schedule.getStage().getOrder() == 1) {
            throw new InvalidRequestException("First stage cannot be assigned interviews");
        }

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(
                () -> new ResourceNotFoundException("Interview not found")
        );

        if (interview.getSchedule() != null) {
            throw new InvalidRequestException("Interview is already assigned to a schedule");
        }

        interview.setSchedule(schedule);
        interviewRepository.save(interview);
    }

    public Page<InterviewDto> getAssignedInterviewsOfSchedule(Long jobId, Long jobStageId, Long scheduleId, Pageable pageable) {
        fetchSchedule(jobId, jobStageId, scheduleId);
        Page<Interview> interviewPage = interviewRepository.findByScheduleId(scheduleId, pageable);

        return interviewPage.map(interviewMapper::toInterviewDto);
    }


    @Transactional
    public void acceptOrRejectInterview(Long jobId, Long stageId, Long scheduleId, Long interviewId, Boolean accepted) {
        Schedule schedule = fetchSchedule(jobId, stageId, scheduleId);
        if (schedule.getJob().getStatus() != JobStatus.OPEN) {
            throw new InvalidRequestException("Job is not open for accepting or rejecting interviews");
        }

        Interview interview = interviewRepository.findById(interviewId).orElseThrow(
                () -> new ResourceNotFoundException("Interview not found")
        );

        if (interview.getSchedule() == null) {
            throw new InvalidRequestException("Interview is not assigned to a schedule");
        }

        if (interview.getStatus() != InterviewStatus.PROGRESS) {
            throw new InvalidRequestException("Interview is not in progress");
        }

        if (accepted) {
            interview.setStatus(InterviewStatus.PASSED);

            Interview nextInterview = new Interview();
            nextInterview.setApplication(interview.getApplication());
            nextInterview.setStatus(InterviewStatus.PROGRESS);

            int nextOrderStage = interview.getStage().getOrder() + 1;
            Stage nextStage = schedule.getJob().getProcess().getStages()
                    .stream()
                    .filter(stage -> stage.getOrder() == nextOrderStage).findFirst()
                    .orElse(null);
            boolean isFinalStage = nextStage == null;

            if (isFinalStage) {
                interview.getApplication().setStatus(ApplicationStatus.ACCEPTED);
                interviewRepository.save(interview);
            } else {
                nextInterview.setStage(nextStage);
                nextInterview.setStatus(InterviewStatus.PROGRESS);

                interviewRepository.save(interview);
                interviewRepository.save(nextInterview);
            }
        } else {
            interview.setStatus(InterviewStatus.FAILED);
            Application rejectedApplication = interview.getApplication();
            rejectedApplication.setStatus(ApplicationStatus.REJECTED);

            interviewRepository.save(interview);
            applicationRepository.save(rejectedApplication);
        }
    }


    private Schedule fetchSchedule(Long jobId, Long stageId, Long scheduleId) {
        return scheduleRepository.findByJobIdAndStageIdAndId(jobId, stageId, scheduleId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Schedule with ID %d not found for job %d and stage %d", scheduleId, jobId, stageId)
                )
        );
    }
}
