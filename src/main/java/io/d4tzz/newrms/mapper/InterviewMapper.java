package io.d4tzz.newrms.mapper;

import io.d4tzz.newrms.dto.interview.InterviewDto;
import io.d4tzz.newrms.entity.Application;
import io.d4tzz.newrms.entity.Interview;
import io.d4tzz.newrms.entity.enums.ApplicationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface InterviewMapper {
    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "coverLetter", source = "application.coverLetter")
    @Mapping(target = "note", source = "application.note")
    @Mapping(target = "cvUrl", source = "application.cvUrl")
    @Mapping(target = "jobId", source = "application.job.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "application.createdAt")
    @Mapping(target = "updatedAt", source = "application.updatedAt")
    @Mapping(target = "interviewId", source = "id")
    InterviewDto toInterviewDto(Interview interview);
}
