package org.example.rms.mapper.candidate;

import org.example.rms.dto.candidate.ApplyJobRequest;
import org.example.rms.dto.candidate.ApplyJobResponse;
import org.example.rms.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplyJobMapper {
    Application toApplication(ApplyJobRequest request);

    @Mapping(source = "job.id", target = "jobId")
    ApplyJobResponse toApplyJobResponse(Application application);
}
