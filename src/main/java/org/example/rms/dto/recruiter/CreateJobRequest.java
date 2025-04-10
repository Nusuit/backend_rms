package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class CreateJobRequest {
    String title;
    String description;
    String requirement;
    String benefit;
    String salary;
    LocalDateTime deadline;

    Set<SkillOfJobRequest> skills;

    @Getter
    @Builder
    public static class SkillOfJobRequest {
        Long id;
        boolean required;
    }
}
