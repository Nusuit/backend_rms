package io.d4tzz.newrms.mapper;

import io.d4tzz.newrms.dto.job.JobSkillDto;
import io.d4tzz.newrms.entity.JobSkill;
import io.d4tzz.newrms.entity.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    @Mapping(target = "jobSkillId", source = "id")
    @Mapping(target = "name", source = "skill.name")
    @Mapping(target = "required", source = "required")
    JobSkillDto toSkillDto(JobSkill jobSkill);
}
