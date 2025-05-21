package io.d4tzz.newrms.dto.interview;

import io.d4tzz.newrms.entity.enums.InterviewStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InterviewDto {
    private Long interviewId;
    private InterviewStatus status;

    private Long applicationId;
    private String coverLetter;
    private String note;
    private String cvUrl;
    private Long jobId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
