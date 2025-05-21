package io.d4tzz.newrms.dto.schedule;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleDto {
    @JsonProperty("scheduleId")
    private Long id;
    private String name;
    private LocalDateTime startTime;
    private String location;
    private String interviewerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long jobId;
    private Long jobStageId;
}
