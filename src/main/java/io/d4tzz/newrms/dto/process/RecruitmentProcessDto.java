package io.d4tzz.newrms.dto.process;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.d4tzz.newrms.dto.stage.StageDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecruitmentProcessDto {
    @JsonProperty("recruitmentProcessId")
    private Long id;

    private String name;

    private List<StageDto> stages;
}
