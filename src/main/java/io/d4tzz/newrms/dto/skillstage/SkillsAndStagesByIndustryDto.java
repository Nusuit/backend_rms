package io.d4tzz.newrms.dto.skillstage;

import io.d4tzz.newrms.dto.skill.SkillByIndustryDto;
import io.d4tzz.newrms.dto.stage.StageByIndustryDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SkillsAndStagesByIndustryDto {
    List<SkillByIndustryDto> skills;
    List<StageByIndustryDto> stages;
}
