package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterviewStageResponse {
    Long id;
    String stageName;
}
