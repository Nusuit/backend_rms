package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateInterviewScheduleResponse {
    Long id;
    String location;
    String interviewerName;
    String type;
    String stageName;
}
