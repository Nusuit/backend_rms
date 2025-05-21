package io.d4tzz.newrms.dto.application;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationScheduleDto {
    private Long scheduleId;
    private String location;
    private LocalDateTime startTime;
}
