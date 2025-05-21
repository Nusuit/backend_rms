package io.d4tzz.newrms.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateScheduleRequest {
    @NotBlank(message = "Schedule name is required")
    private String name;
    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "dd/MM/yyy HH:mm:ss")
    private LocalDateTime startTime;
    @NotBlank(message = "Location is required")
    private String location;
    @NotBlank(message = "Interviewer name is required")
    private String interviewerName;
}
