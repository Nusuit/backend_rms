package org.example.rms.dto.candidate;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder(toBuilder = true)
public class CandidateJobResponse {
    Long id;
    boolean applicable;
    String title;
    String description;
    String requirement;
    String benefit;
    String salary;
    LocalDateTime deadline;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    Set<SkillOfJobResponse> skills;

    @Data
    public static class SkillOfJobResponse {
        Long id;
        String skillName;
        boolean required;
    }
}
