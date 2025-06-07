package io.d4tzz.newrms.dto.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.d4tzz.newrms.entity.enums.JobType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateJobRequest {
    private String title;
    private String department;
    private JobType type;
    private String description;
    private String requirement;
    private String benefit;
    @JsonProperty("salaryMin")
    private Long minSalary;
    @JsonProperty("salaryMax")
    private Long maxSalary;
    private String salaryCurrency;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    private String location;

    private List<SkillRequirement> skills;
    private Long processId;
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
