package io.d4tzz.newrms.dto.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationStageDto {
    private Long stageId;
    private String stageName;

    private ApplicationInterviewDto interview;
    private ApplicationScheduleDto schedule;
}
