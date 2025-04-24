package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.InterviewRecordResponse;
import org.example.rms.entity.InterviewRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InterviewRecordMapper {
    @Mapping(source = "application.candidate.name", target = "candidateName")
    @Mapping(source = "application.candidate.gender", target = "gender")
    @Mapping(source = "application.candidate.phone", target = "phone")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "passed", target = "passed")
    InterviewRecordResponse toInterviewRecordResponse(InterviewRecord interviewRecord);
}
