package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateStageOfJobResponse {
    Long id;
    int stageOrder;
    String stageName;
}
