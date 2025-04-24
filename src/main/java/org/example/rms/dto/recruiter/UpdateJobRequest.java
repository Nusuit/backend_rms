package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateJobRequest {
    String title;
    String description;
    String requirement;
    String benefit;
    String salary;
    LocalDateTime deadline;
}
