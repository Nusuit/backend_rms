package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.CreateJobRequest;
import org.example.rms.entity.Job;
import org.example.rms.entity.SkillOfJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateJobMapper {

    @Mapping(source = "id", target = "skill.id")
    SkillOfJob toSkillOfJob(CreateJobRequest.SkillOfJobRequest request);

    Job toJob(CreateJobRequest request);
}
