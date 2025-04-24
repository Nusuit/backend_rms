package org.example.rms.mapper.candidate;

import org.example.rms.dto.candidate.CandidateApplicationResponse;
import org.example.rms.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateApplicationMapper {
    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "candidate.cvUrl", target = "cvUrl")
    CandidateApplicationResponse toCandidateApplicationResponse(Application application);
}
