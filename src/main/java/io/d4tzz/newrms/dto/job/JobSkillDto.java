package io.d4tzz.newrms.dto.job;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobSkillDto {
    private Long jobSkillId;
    private String name;
    private boolean required;
}
