package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class JobForRecruiterResponse {
    Long id;
    String title;
    String description;
    String requirement;
    String benefit;
    String salary;
    LocalDateTime deadline;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    Set<SkillOfJobResponse> skills;

    @Getter
    @Builder
    public static class SkillOfJobResponse {
        Long id;
        String skillName;
        boolean required;
    }
}
