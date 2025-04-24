package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.InterviewScheduleResponse;
import org.example.rms.entity.InterviewSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InterviewScheduleMapper {
    @Mapping(source = "stage.stageName", target = "stageName")
    InterviewScheduleResponse toInterviewScheduleResponse(InterviewSchedule interviewSchedule);
}
