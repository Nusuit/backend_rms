package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateInterviewScheduleRequest {
    String location;
    String interviewerName;
    String type;
    LocalDateTime time;
}
