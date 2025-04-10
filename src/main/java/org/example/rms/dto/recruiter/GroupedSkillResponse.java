package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class GroupedSkillResponse {
    String group;
    List<SkillResponse> skills;

    @Getter
    @Setter
    public static class SkillResponse {
        Long id;
        String skillName;
    }
}
