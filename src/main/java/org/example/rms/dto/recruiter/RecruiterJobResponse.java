package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class RecruiterJobResponse {
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
    List<StageOfJobResponse> stages;

    @Getter
    @Builder
    public static class SkillOfJobResponse {
        Long id;
        String skillName;
        boolean required;
    }

    @Getter
    @Builder
    public static class StageOfJobResponse {
        Long id;
        String stageOrder;
        String stageName;
    }
}
