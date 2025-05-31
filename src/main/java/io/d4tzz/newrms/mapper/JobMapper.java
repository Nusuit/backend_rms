package io.d4tzz.newrms.mapper;


import io.d4tzz.newrms.dto.job.*;
import io.d4tzz.newrms.entity.*;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {})
public interface JobMapper {

    @Mapping(target = "jobSkillId", source = "id")
    @Mapping(target = "name", source = "skill.name")
    @Mapping(target = "required", source = "required")
    JobSkillDto toSkillDto(JobSkill jobSkill);

    @Mapping(target = "stageId", source = "id")
    @Mapping(target = "name", source = "stage.name")
    @Mapping(target = "order", source = "order")
    JobStageDto toStageDto(Stage stage);

    @Mapping(target = "applicationQuantity", source = "applicationQuantity")
    @Mapping(target = "stages", source = "job.process.stages")
    RecruiterJobDto toRecruiterJobDto(Job job, long applicationQuantity);

    CandidateJobDto toCandidateJobDto(Job job, boolean applicable);


    Job toJob(CreateJobRequest createJobRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartial(UpdateJobRequest updateJobRequest, @MappingTarget Job job);
}
