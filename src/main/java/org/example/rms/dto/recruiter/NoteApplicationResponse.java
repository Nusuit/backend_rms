package org.example.rms.dto.recruiter;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoteApplicationResponse {
    Long id;
    String coverLetter;
    String status;
    String recruiterNote;
    String cvUrl;
}
