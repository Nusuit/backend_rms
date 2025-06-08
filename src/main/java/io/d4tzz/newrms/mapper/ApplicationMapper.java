package io.d4tzz.newrms.mapper;

import io.d4tzz.newrms.dto.application.ApplicationDto;
import io.d4tzz.newrms.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

//    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "candidateName", source = "candidate.name")
    @Mapping(target = "candidateEmail", source = "candidate.auth.email")
    ApplicationDto toApplicationDto(Application application);
}
