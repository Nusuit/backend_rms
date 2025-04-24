package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateSkillOfJobResponse {
    Long id;
    String skillName;
    boolean required;
}
