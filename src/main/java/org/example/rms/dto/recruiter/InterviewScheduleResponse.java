package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;
import org.example.rms.entity.InterviewStage;
import org.example.rms.entity.Job;

import java.time.LocalDateTime;

@Getter
@Builder
public class InterviewScheduleResponse {
    Long id;
    String location;
    String interviewerName;
    String type;
    String stageName; // InterviewStage.stageName
    LocalDateTime time;
}
