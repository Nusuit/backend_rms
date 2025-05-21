package io.d4tzz.newrms.dto.application;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApplicationJobDto {
    private Long jobId;
    private String title;

    private List<ApplicationStageDto> stages;
}
