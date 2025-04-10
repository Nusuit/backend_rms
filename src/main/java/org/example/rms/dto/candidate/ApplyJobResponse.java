package org.example.rms.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.example.rms.entity.ApplicationStatus;

@Getter
@Builder
public class ApplyJobResponse {
    @JsonProperty("applicationId")
    Long id;
    String coverLetter;
    ApplicationStatus status;
    String recruiterNote;
    Long jobId;
}
