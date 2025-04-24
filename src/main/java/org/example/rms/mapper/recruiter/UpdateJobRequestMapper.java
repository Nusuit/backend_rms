package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.UpdateJobRequest;
import org.example.rms.dto.recruiter.UpdateJobResponse;
import org.example.rms.entity.Job;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UpdateJobRequestMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateJob(UpdateJobRequest request, @MappingTarget Job job);

    UpdateJobResponse toUpdateJobResponse(Job job);
}
