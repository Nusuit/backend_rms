package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.JobForRecruiterResponse;
import org.example.rms.entity.Job;
import org.example.rms.entity.SkillOfJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobForRecruiterMapper {
    @Mapping(source = "skill.skillName", target = "skillName")
    @Mapping(source = "skill.id", target = "id")
    JobForRecruiterResponse.SkillOfJobResponse toSkillOfJobResponse(SkillOfJob skill);

    JobForRecruiterResponse toGetJobResponse(Job job);

}
