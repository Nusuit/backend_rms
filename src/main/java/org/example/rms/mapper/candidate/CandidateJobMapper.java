package org.example.rms.mapper.candidate;

import org.example.rms.dto.candidate.CandidateJobResponse;
import org.example.rms.entity.Job;
import org.example.rms.entity.SkillOfJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateJobMapper {
    @Mapping(source = "skill.skillName", target = "skillName")
    @Mapping(source = "skill.id", target = "id")
    CandidateJobResponse.SkillOfJobResponse toSkillOfJobResponse(SkillOfJob skill);


    CandidateJobResponse toCandidateJobResponse(Job job);
}
