package org.example.rms.mapper.candidate;

import org.example.rms.dto.candidate.JobForCandidateResponse;
import org.example.rms.entity.Job;
import org.example.rms.entity.SkillOfJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface JobForCandidateMapper {
    @Mapping(source = "skill.skillName", target = "skillName")
    @Mapping(source = "skill.id", target = "id")
    JobForCandidateResponse.SkillOfJobResponse toSkillOfJobResponse(SkillOfJob skill);


    JobForCandidateResponse toGetJobResponse(Job job);
}
