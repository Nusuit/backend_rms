package io.d4tzz.newrms.mapper;

import io.d4tzz.newrms.dto.interview.InterviewDto;
import io.d4tzz.newrms.entity.Application;
import io.d4tzz.newrms.entity.Interview;
import io.d4tzz.newrms.entity.enums.ApplicationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Mapper(componentModel = "spring")
public interface InterviewMapper {
    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "coverLetter", source = "application.coverLetter")
    @Mapping(target = "note", source = "application.note")
    @Mapping(target = "cvUrl", source = "application.cvUrl")
    
    @Mapping(target = "candidateFullName", source = "application.candidate.name")
    @Mapping(target = "candidateEmail", source = "application.candidate.auth.email")
    
    @Mapping(target = "jobId", source = "application.job.id")
    @Mapping(target = "jobTitle", source = "application.job.title")
    @Mapping(target = "jobDepartment", source = "application.job.department")
    
    @Mapping(target = "stageId", source = "stage.id")
    @Mapping(target = "stageName", source = "stage.name")
    
    @Mapping(target = "scheduleId", source = "schedule.id")
    @Mapping(target = "scheduleName", source = "schedule.name")
    @Mapping(target = "scheduleStartTime", source = "schedule.startTime")
    @Mapping(target = "scheduleLocation", source = "schedule.location")
    @Mapping(target = "interviewerName", source = "schedule.interviewerName")
    
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "interviewId", source = "id")
    InterviewDto toInterviewDto(Interview interview);
}
