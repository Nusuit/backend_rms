package io.d4tzz.newrms.controller.schedule;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.interview.InterviewDto;
import io.d4tzz.newrms.dto.schedule.CreateScheduleRequest;
import io.d4tzz.newrms.dto.schedule.ScheduleDto;
import io.d4tzz.newrms.dto.schedule.UpdateScheduleRequest;
import io.d4tzz.newrms.service.schedule.RecruiterScheduleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterScheduleController {
    private final RecruiterScheduleServiceImpl recruiterScheduleServiceImpl;

    public RecruiterScheduleController(RecruiterScheduleServiceImpl recruiterScheduleServiceImpl) {
        this.recruiterScheduleServiceImpl = recruiterScheduleServiceImpl;
    }

    @PostMapping("/jobs/{jobId}/stages/{stageId}/schedules")
    public ApiResponse<?> createSchedule(@Valid @RequestBody CreateScheduleRequest request, @PathVariable Long jobId, @PathVariable Long stageId) {
        ScheduleDto scheduleDto = recruiterScheduleServiceImpl.createSchedule(jobId, stageId, request);

        return ApiResponse.success(scheduleDto);
    }

    @GetMapping("/jobs/{jobId}/stages/{stageId}/schedules")
    public ApiResponse<?> getSchedules(@PathVariable Long jobId, @PathVariable Long stageId, Pageable pageable) {
        return ApiResponse.success(recruiterScheduleServiceImpl.getSchedules(jobId, stageId, pageable));
    }

    @PutMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}")
    public ApiResponse<?> updateSchedule(@PathVariable Long jobId, @PathVariable Long stageId,
                                         @PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequest request) {
        ScheduleDto scheduleDto = recruiterScheduleServiceImpl.updateSchedule(jobId, stageId, scheduleId, request);

        return ApiResponse.success(scheduleDto);
    }

    @GetMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/interviews/unassigned")
    public ApiResponse<?> getInterviewsForSchedule(@PathVariable Long jobId, @PathVariable Long stageId,
                                                   @PathVariable Long scheduleId, Pageable pageable) {
        Page<InterviewDto> interviewDtoDtoPage =
                recruiterScheduleServiceImpl.getInterviewsForSchedule(jobId, stageId, scheduleId, pageable);

        return ApiResponse.success(interviewDtoDtoPage);
    }

    @PostMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/interviews/{interviewId}/assign")
    public ApiResponse<?> assignInterviewsToSchedule(@PathVariable Long jobId, @PathVariable Long stageId,
                                                     @PathVariable Long scheduleId, @PathVariable Long interviewId) {
        recruiterScheduleServiceImpl.assignInterviewsToSchedule(jobId, stageId, scheduleId, interviewId);

        return ApiResponse.success(null, "Applications assigned to schedule");
    }

    @GetMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/interviews")
    public ApiResponse<?> getAssignedInterviewsOfSchedule(@PathVariable Long jobId, @PathVariable Long stageId,
                                                         @PathVariable Long scheduleId, Pageable pageable) {
        Page<InterviewDto> interviewDtoPage =
                recruiterScheduleServiceImpl.getAssignedInterviewsOfSchedule(jobId, stageId, scheduleId, pageable);

        return ApiResponse.success(interviewDtoPage);
    }

    @PostMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/interviews/{interviewId}")
    public ApiResponse<?> acceptOrRejectInterview(@PathVariable Long jobId, @PathVariable Long stageId,
                                                  @PathVariable Long scheduleId, @PathVariable Long interviewId,
                                                  @RequestParam("accept") boolean accept) {
        recruiterScheduleServiceImpl.acceptOrRejectInterview(jobId, stageId, scheduleId, interviewId, accept);

        return ApiResponse.success();
    }

    @PostMapping("/jobs/{jobId}/stages/{stageId}/interviews/{interviewId}")
    public ApiResponse<?> acceptOrRejectInterviewWithoutSchedule(@PathVariable Long jobId, @PathVariable Long stageId,
                                                  @PathVariable Long interviewId, @RequestParam("accept") boolean accept) {
        recruiterScheduleServiceImpl.acceptOrRejectInterview(jobId, stageId, null, interviewId, accept);
        return ApiResponse.success();
    }

    @GetMapping("/interviews")
    public ApiResponse<?> getAllInterviews(Pageable pageable) {
        Page<InterviewDto> interviewDtoPage = recruiterScheduleServiceImpl.getAllInterviews(pageable);
        return ApiResponse.success(interviewDtoPage);
    }
}
