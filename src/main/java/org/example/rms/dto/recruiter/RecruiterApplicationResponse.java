package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;
import org.example.rms.entity.ApplicationStatus;

@Getter
@Builder
public class RecruiterApplicationResponse {
    Long id;
    String coverLetter;
    ApplicationStatus status;
    String recruiterNote;
    String cvUrl;
}
