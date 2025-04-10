package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.ApplicationForRecruiterResponse;
import org.example.rms.entity.Application;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationForRecruiterMapper {
    ApplicationForRecruiterResponse toApplicationResponse(Application application);
}
