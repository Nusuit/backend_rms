package org.example.rms.controller;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.rms.dto.ApiResponse;
import org.example.rms.dto.recruiter.*;
import org.example.rms.entity.ApplicationStatus;
import org.example.rms.security.UserPrincipal;
import org.example.rms.service.RecruiterServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class RecruiterController {
    private final RecruiterServiceImp recruiterService;


    @GetMapping("/jobs")
    public ApiResponse<?> getJobs(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Page<RecruiterJobResponse> jobResponsePage = recruiterService.getJobs(userPrincipal.getIdentity(), pageable);

        return ApiResponse
                .builder()
                .success(true)
                .payload(jobResponsePage)
                .build();
    }

    @GetMapping("/jobs/{jobId}")
    public ApiResponse<?> getJob(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long jobId) {
        RecruiterJobResponse getJobResponse = recruiterService.getJob(userPrincipal.getIdentity(), jobId);
        return ApiResponse
                .builder()
                .success(true)
                .payload(getJobResponse)
                .build();
    }

    @GetMapping("application-status")
    public ApiResponse<?> getApplicationStatus() {
        List<ApplicationStatus> response = recruiterService.getApplicationStatuses();

        return ApiResponse.successBuild(response);
    }

    @GetMapping("/jobs/{jobId}/applications")
    public ApiResponse<?> getApplicationsOfJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long jobId,
                                               @RequestParam(required = false) List<ApplicationStatus> statuses,
                                               Pageable pageable) {
        Page<RecruiterApplicationResponse> applicationResponsePage =
                recruiterService.getApplicationsOfJob(jobId, userPrincipal.getIdentity(), statuses, pageable);

        return ApiResponse.successBuild(applicationResponsePage);
    }

    @GetMapping("/applications/{applicationId}")
    public ApiResponse<?> getApplication(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @PathVariable Long applicationId) {
        RecruiterApplicationResponse applicationResponse = recruiterService.getApplicationOfJob(applicationId, userPrincipal.getIdentity());

        return ApiResponse.successBuild(applicationResponse);
    }

    @GetMapping("/skills")
    public ApiResponse<?> getSkills() {
        List<GroupedSkillResponse> groupedSkills = recruiterService.getSkills();

        return ApiResponse.successBuild(groupedSkills);
    }

    @GetMapping("/stages")
    public ApiResponse<?> getInterviewStages() {
        List<InterviewStageResponse> response = recruiterService.getInterviewStages();

        return ApiResponse.successBuild(response);
    }

    @PostMapping("/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> createJob(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CreateJobRequest postJobRequest) {
        CreateJobResponse response = recruiterService.createJob(userPrincipal.getIdentity(), postJobRequest);

        return ApiResponse.successBuild(response);
    }

    @PutMapping("/jobs/{jobId}")
    public ApiResponse<?> updateJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @PathVariable Long jobId,
                                    @RequestBody UpdateJobRequest request) {
        UpdateJobResponse response = recruiterService.updateJob(userPrincipal.getIdentity(), jobId, request);

        return ApiResponse.successBuild(response);
    }

    @PatchMapping("/jobs/{jobId}/applications/{applicationId}")
    public ApiResponse<?> isQualifiedApplication(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PathVariable Long jobId, @PathVariable Long applicationId,
                                                 @RequestParam boolean isQualified) {
        recruiterService.isQualifiedApplication(userPrincipal.getIdentity(), jobId, applicationId, isQualified);

        return ApiResponse.successBuild();
    }

    @PostMapping("/jobs/{jobId}/stages/{stageId}/schedules")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> createInterviewSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @PathVariable Long jobId, @PathVariable Long stageId,
                                                  @RequestBody CreateInterviewScheduleRequest request) {
        CreateInterviewScheduleResponse response =
                recruiterService.createInterviewSchedule(userPrincipal.getIdentity(), jobId, stageId, request);

        return ApiResponse.successBuild(response);
    }

    @GetMapping("/jobs/{jobId}/stages/{stageId}/schedules")
    public ApiResponse<?> getInterviewSchedules(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PathVariable Long jobId, @PathVariable Long stageId, Pageable pageable) {
        Page<InterviewScheduleResponse> response =
                recruiterService.getInterviewSchedules(userPrincipal.getIdentity(), jobId, stageId, pageable);

        return ApiResponse.successBuild(response);
    }

    @GetMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/interview-records")
    public ApiResponse<?> getInterviewRecordsForSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @PathVariable Long jobId, @PathVariable Long stageId,
                                                     @PathVariable Long scheduleId, Pageable pageable) {
        Page<InterviewRecordResponse> response =
                recruiterService.getInterviewRecordsForSchedule(userPrincipal.getIdentity(), jobId,stageId, scheduleId, pageable);

        return ApiResponse.successBuild(response);
    }

    @PatchMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/interview-records")
    public ApiResponse<?> assignInterviewRecordToSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @PathVariable Long jobId, @PathVariable Long stageId,
                                                      @PathVariable Long scheduleId,
                                                      @RequestBody List<AssignedInterviewRecord> request) {
        var response = recruiterService.assignInterviewRecordToSchedule(userPrincipal.getIdentity(), jobId, stageId, scheduleId, request);

        return ApiResponse.successBuild(response);
    }

    @GetMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/scheduled-interview-records")
    public ApiResponse<?> getInterviewRecordsOfSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @PathVariable Long jobId, @PathVariable Long stageId,
                                                    @PathVariable Long scheduleId, Pageable pageable) {
        Page<InterviewRecordResponse> response =
                recruiterService.getInterviewRecordsOfSchedule(userPrincipal.getIdentity(), jobId, stageId, scheduleId, pageable);

        return ApiResponse.successBuild(response);
    }

    @PatchMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/interviews/{interviewId}")
    public ApiResponse<?> noteInterviewRecord(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable Long jobId, @PathVariable Long stageId,
                                              @PathVariable Long scheduleId, @PathVariable Long interviewId,
                                              @RequestBody NoteInterviewRecordRequest request) {
        NoteInterviewRecordResponse response =
                recruiterService.noteInterviewRecord(userPrincipal.getIdentity(), jobId, stageId, scheduleId, interviewId, request);

        return ApiResponse.successBuild(response);
    }

    @PatchMapping("/jobs/{jobId}/stages/{stageId}/schedules/{scheduleId}/next-stage")
    public ApiResponse<?> moveToNextStage(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long jobId, @PathVariable Long stageId,
                                          @PathVariable Long scheduleId, @RequestBody List<NextInterviewStageRequest> request) throws MessagingException {
        var response = recruiterService.moveToNextStage(userPrincipal.getIdentity(), jobId, stageId, scheduleId, request);

        return ApiResponse.successBuild(response);
    }


    @PutMapping("/jobs/{jobId}/skills")
    public ApiResponse<?> updateSkillsOfJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long jobId, @RequestBody List<UpdateSkillOfJobRequest> request) {
        List<UpdateSkillOfJobResponse> response = recruiterService.updateSkillsOfJob(userPrincipal.getIdentity(), jobId, request);

        return ApiResponse.successBuild(response);
    }

//    @PutMapping("/jobs/{jobId}/stages")
//    public ApiResponse<?> updateStagesOfJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
//                                            @PathVariable Long jobId, @RequestBody List<UpdateStageOfJobRequest> request) {
//        List<UpdateStageOfJobResponse> response = recruiterService.updateStagesOfJob(userPrincipal.getIdentity(), jobId, request);
//
//        return ApiResponse.successBuild(response);
//    }

    @DeleteMapping("/jobs/{jobId}")
    public ApiResponse<?> deleteJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @PathVariable Long jobId) {
        recruiterService.deleteJob(userPrincipal.getIdentity(), jobId);

        return ApiResponse.successBuild();
    }

    @PatchMapping("/jobs/{jobId}/applications/{applicationId}/note")
    public ApiResponse<?> noteApplication(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable Long applicationId, @PathVariable Long jobId,
                                          @RequestBody NoteApplicationRequest request) {
        NoteApplicationResponse response =
                recruiterService.noteApplication(userPrincipal.getIdentity(), jobId, applicationId, request);

        return ApiResponse.successBuild(response);
    }

//    @PatchMapping("/applications/{applicationId}/status")
//    public ApiResponse<?> setApplicationStatus(@AuthenticationPrincipal UserPrincipal userPrincipal,
//                                               @PathVariable Long applicationId,
//                                               @RequestParam ApplicationStatus status) {
//        recruiterService.setApplicationStatus(userPrincipal.getIdentity(), applicationId, status);
//
//        return ApiResponse.successBuild();
//    }
}
