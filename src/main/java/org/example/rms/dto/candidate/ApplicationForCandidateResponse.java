package org.example.rms.dto.candidate;

import lombok.Builder;
import lombok.Getter;
import org.example.rms.entity.ApplicationStatus;

@Getter
@Builder
public class ApplicationForCandidateResponse {
    Long id;
    String coverLetter;
    ApplicationStatus status;
    String recruiterNote;
    Long jobId;
}
