package io.d4tzz.newrms.dto.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.d4tzz.newrms.entity.enums.JobType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateJobRequest {
    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 100, message = "Job title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Job type is required")
    private JobType type;

    @NotBlank(message = "Job description is required")
    @Size(min = 10, message = "Job description must be at least 10 characters")
    private String description;

    @NotBlank(message = "Job requirements are required")
    private String requirements;

    private String benefits;

    @Size(max = 1000, message = "Responsibilities must not exceed 1000 characters")
    private String responsibilities;

    @NotNull(message = "Minimum salary is required")
    @Min(value = 0, message = "Minimum salary must be greater than or equal to 0")
    @JsonProperty("salaryMin")
    private Long minSalary;

    @NotNull(message = "Maximum salary is required")
    @Min(value = 0, message = "Maximum salary must be greater than or equal to 0")
    @JsonProperty("salaryMax")
    private Long maxSalary;

    @NotBlank(message = "Salary currency is required")
    @Size(min = 3, max = 3, message = "Salary currency must be a 3-letter code")
    private String salaryCurrency;

    @NotNull(message = "Application deadline is required")
    @Future(message = "Application deadline must be a future date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @NotBlank(message = "Job location is required")
    private String location;

    @NotEmpty(message = "At least one skill requirement is required")
    @Valid
    private List<SkillRequirement> skills;

    @NotNull(message = "Recruitment process is required")
    private Long processId;

    @NotEmpty(message = "At least one stage requirement is required")
    @Valid
    private List<StageRequirement> stages;

    @Getter
    @Builder
    public static class SkillRequirement {
        @NotNull(message = "Skill ID is required")
        private Long skillId;
        private boolean required;
    }

    @Getter
    @Builder
    public static class StageRequirement {
        @NotNull(message = "Stage ID is required")
        private Long stageId;
    }
}
