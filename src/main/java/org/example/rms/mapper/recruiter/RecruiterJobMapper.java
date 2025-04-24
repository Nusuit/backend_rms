package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.RecruiterJobResponse;
import org.example.rms.entity.Job;
import org.example.rms.entity.SkillOfJob;
import org.example.rms.entity.StageOfJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecruiterJobMapper {
    @Mapping(source = "skill.skillName", target = "skillName")
    @Mapping(source = "skill.id", target = "id")
    RecruiterJobResponse.SkillOfJobResponse toSkillOfJobResponse(SkillOfJob skill);

    @Mapping(source = "stage.id", target = "id")
    @Mapping(source = "stage.stageName", target = "stageName")
    @Mapping(source = "stageOrder", target = "stageOrder")
    RecruiterJobResponse.StageOfJobResponse toStageOfJobResponse(StageOfJob stage);

    RecruiterJobResponse toRecruiterJobResponse(Job job);

}
