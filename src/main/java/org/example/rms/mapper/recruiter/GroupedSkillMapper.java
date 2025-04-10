package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.GroupedSkillResponse;
import org.example.rms.entity.Skill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupedSkillMapper {
    GroupedSkillResponse.SkillResponse toSkillResponse(Skill skill);
}
