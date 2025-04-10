package org.example.rms.mapper.candidate;

import org.example.rms.dto.candidate.ApplicationForCandidateResponse;
import org.example.rms.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationForCandidateMapper {
    @Mapping(source = "job.id", target = "jobId")
    ApplicationForCandidateResponse toApplicationForCandidateResponse(Application application);
}
