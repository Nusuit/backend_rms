package io.d4tzz.newrms.dto.skill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillByIndustryDto {
    @JsonProperty("skillId")
    private Long id;
    private String name;
}
