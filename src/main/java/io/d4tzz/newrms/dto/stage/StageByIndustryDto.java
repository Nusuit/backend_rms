package io.d4tzz.newrms.dto.stage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StageByIndustryDto {
    @JsonProperty("stageId")
    private Long id;
    private String name;
    private Integer order;
    private boolean skipped;
}
