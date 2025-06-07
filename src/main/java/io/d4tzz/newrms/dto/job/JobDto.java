package io.d4tzz.newrms.dto.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.d4tzz.newrms.entity.enums.JobStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
public class JobDto {
    @JsonProperty("jobId")
    private Long id;
    private String title;
    private String description;
    private String requirement;
    private String benefit;
    private Long minSalary;
    private Long maxSalary;
    private String salaryCurrency;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    private String location;
    private String note;
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<JobSkillDto> skills;
    private List<JobStageDto> stages;
}
