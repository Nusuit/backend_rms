package io.d4tzz.newrms.dto.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateJobRequest {
    private String title;
    private String description;
    private String requirement;
    private String benefit;
    private Long minSalary;
    private Long maxSalary;
    private String salaryCurrency;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate deadline;
    private String location;

    private List<SkillRequirement> skills;
    private List<StageRequirement> stages;

    @Getter
    @Builder
    public static class SkillRequirement {
        private Long skillId;
        private boolean required;
    }

    @Getter
    @Builder
    public static class StageRequirement {
        private Long stageId;
    }
}
