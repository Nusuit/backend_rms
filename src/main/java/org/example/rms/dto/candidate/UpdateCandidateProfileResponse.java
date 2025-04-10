package org.example.rms.dto.candidate;

import lombok.Builder;
import lombok.Getter;
import org.example.rms.entity.Gender;

import java.time.LocalDate;

@Getter
@Builder
public class UpdateCandidateProfileResponse {
    Long id;
    String name;
    Gender gender;
    LocalDate dateOfBirth;
    String phone;
    String address;
    String cvUrl;
    String profilePictureUrl;
    String summary;
}
