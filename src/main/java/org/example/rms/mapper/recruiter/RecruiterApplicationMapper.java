package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.RecruiterApplicationResponse;
import org.example.rms.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecruiterApplicationMapper {
    @Mapping(source = "candidate.cvUrl", target = "cvUrl")
    RecruiterApplicationResponse toRecruiterApplicationResponse(Application application);
}
