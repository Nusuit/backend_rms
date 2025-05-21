package io.d4tzz.newrms.dto.candidate;

import io.d4tzz.newrms.entity.enums.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CandidateDto {
    String name;
    Gender gender;
    LocalDate dateOfBirth;
    String phone;
    String address;
    String cvUrl;
    String avatarUrl;
}
