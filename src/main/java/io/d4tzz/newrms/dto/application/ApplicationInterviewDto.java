package io.d4tzz.newrms.dto.application;

import io.d4tzz.newrms.entity.enums.InterviewStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationInterviewDto {
    private Long interviewId;
    private InterviewStatus status;
}
