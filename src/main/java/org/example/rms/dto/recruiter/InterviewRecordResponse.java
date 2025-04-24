package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterviewRecordResponse {
    Long id;
    String candidateName; // Application.Candidate.name
    String gender; // Application.Candidate.gender
    String phone; // Application.Candidate.phone
    String note;
    boolean passed;
}
