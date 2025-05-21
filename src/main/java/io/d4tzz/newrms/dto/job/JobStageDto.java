package io.d4tzz.newrms.dto.job;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobStageDto {
    private Long jobStageId;
    private String name;
    private int order;
}
