package io.d4tzz.newrms.mapper;


import io.d4tzz.newrms.dto.job.CandidateJobDto;
import io.d4tzz.newrms.dto.job.CreateJobRequest;
import io.d4tzz.newrms.dto.job.RecruiterJobDto;
import io.d4tzz.newrms.dto.job.UpdateJobRequest;
import io.d4tzz.newrms.entity.Job;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {SkillMapper.class, StageMapper.class})
public interface JobMapper {

    @Mapping(target = "applicationQuantity", source = "applicationQuantity")
    RecruiterJobDto toRecruiterJobDto(Job job, long applicationQuantity);

    CandidateJobDto toCandidateJobDto(Job job, boolean applicable);

    Job toJob(CreateJobRequest createJobRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartial(UpdateJobRequest updateJobRequest, @MappingTarget Job job);
}
