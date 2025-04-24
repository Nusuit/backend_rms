package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.CreateInterviewScheduleRequest;
import org.example.rms.dto.recruiter.CreateInterviewScheduleResponse;
import org.example.rms.entity.InterviewSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateInterviewScheduleMapper {
    InterviewSchedule toInterviewSchedule(CreateInterviewScheduleRequest request);

    @Mapping(source = "stage.stageName", target = "stageName")
    CreateInterviewScheduleResponse toCreateInterviewScheduleResponse(InterviewSchedule interviewSchedule);
}
