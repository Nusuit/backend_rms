package io.d4tzz.newrms.dto.skill;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillDto {
    @JsonProperty("skillId")
    private Long id;
    private String name;
}
