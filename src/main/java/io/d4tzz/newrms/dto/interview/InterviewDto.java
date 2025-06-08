package io.d4tzz.newrms.dto.interview;

import io.d4tzz.newrms.entity.enums.InterviewStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InterviewDto {
    private Long interviewId;
    private InterviewStatus status;

    // Application Info
    private Long applicationId;
    private String coverLetter;
    private String note;
    private String cvUrl;
    
    // Candidate Info
    private String candidateFullName;
    private String candidateEmail;
    
    // Job Info
    private Long jobId;
    private String jobTitle;
    private String jobDepartment;
    
    // Stage Info
    private Long stageId;
    private String stageName;
    
    // Schedule Info
    private Long scheduleId;
    private String scheduleName;
    private LocalDateTime scheduleStartTime;
    private String scheduleLocation;
    private String interviewerName;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
