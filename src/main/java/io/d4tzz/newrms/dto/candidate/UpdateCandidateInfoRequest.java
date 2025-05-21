package io.d4tzz.newrms.dto.candidate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.d4tzz.newrms.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UpdateCandidateInfoRequest {
    @NotBlank(message = "Name must not blank")
    String name;
    @NotNull(message = "Gender must not blank")
    Gender gender;
    @NotNull(message = "Date of birth must not null")
    LocalDate dateOfBirth;
    @NotBlank(message = "Phone must not blank")
    String phone;
    @NotBlank(message = "Address must not blank")
    String address;
}
