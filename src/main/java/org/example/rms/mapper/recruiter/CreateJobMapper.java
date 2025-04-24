package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.CreateJobRequest;
import org.example.rms.dto.recruiter.CreateJobResponse;
import org.example.rms.entity.Job;
import org.example.rms.entity.StageOfJob;
import org.example.rms.entity.SkillOfJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateJobMapper {

    @Mapping(source = "id", target = "skill.id")
    SkillOfJob toSkillOfJob(CreateJobRequest.SkillOfJobRequest request);

    @Mapping(source = "id", target = "stage.id")
    StageOfJob toStageOfJob(CreateJobRequest.StageOfJobRequest request);

    Job toJob(CreateJobRequest request);


    @Mapping(source = "skill.id", target = "id")
    @Mapping(source = "skill.skillName", target = "skillName")
    CreateJobResponse.SkillOfJobResponse toSkillOfJobResponse(SkillOfJob skills);

    @Mapping(source = "stage.id", target = "id")
    @Mapping(source = "stage.stageName", target = "stageName")
    @Mapping(source = "stageOrder", target = "stageOrder")
    CreateJobResponse.StageOfJobResponse toStageOfJobResponse(StageOfJob stage);

    CreateJobResponse toCreateJobResponse(Job job);
}
