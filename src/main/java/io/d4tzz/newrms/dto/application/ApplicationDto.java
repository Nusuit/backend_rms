package io.d4tzz.newrms.dto.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.d4tzz.newrms.entity.enums.ApplicationStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ApplicationDto {
    @JsonProperty("applicationId")
    private Long id;
    private String coverLetter;
    private String note;
    private String cvUrl;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Candidate information
    private String candidateName;
    private String candidateEmail;

    private ApplicationJobDto job;
}
