package io.d4tzz.newrms.service.application;

import io.d4tzz.newrms.dto.application.*;
import io.d4tzz.newrms.entity.Application;
import io.d4tzz.newrms.entity.Interview;
import io.d4tzz.newrms.entity.Job;
import io.d4tzz.newrms.entity.Schedule;
import io.d4tzz.newrms.entity.enums.JobStatus;
import io.d4tzz.newrms.exception.InvalidRequestException;
import io.d4tzz.newrms.exception.ResourceNotFoundException;
import io.d4tzz.newrms.repository.ApplicationRepository;
import io.d4tzz.newrms.repository.InterviewRepository;
import io.d4tzz.newrms.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateApplicationServiceImpl extends AbstractService implements CandidateApplicationService {
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;



    public Page<ApplicationDto> getApplications(Pageable pageable) {
        pageable = ensureSortedPageable(pageable);
        Page<Application> applicationPage = applicationRepository.findByCandidateId(getUserIdentity(), pageable);

        return applicationPage.map(this::applicationToDto);
    }


    /*
     * Map Application to ApplicationDto
     * each ApplicationDto:
     *  + application information (id, ...)
     *  + jobDto:
     *      + job information (id, ...)
     *      + stages:
     *          + interviewDto
     *          + scheduleDto
     */
    private ApplicationDto applicationToDto(Application application) {
        Job job = application.getJob();

        List<ApplicationStageDto> applicationStageDtos = job.getProcess().getStages()
                .stream()
                .map(stage -> {

                    Interview interview = interviewRepository.findByApplicationIdAndStageId(application.getId(), stage.getId())
                            .orElse(null);
                    ApplicationInterviewDto applicationInterviewDto = null;
                    if (interview != null) {
                        applicationInterviewDto = ApplicationInterviewDto.builder()
                                .interviewId(interview.getId())
                                .status(interview.getStatus())
                                .build();
                    }

                    Schedule schedule = interview == null ? null : interview.getSchedule();
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

                })
                .toList();


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
}
