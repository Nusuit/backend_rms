package io.d4tzz.newrms.dto.companyprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CompanyProfileDto {
    @JsonProperty("id")
    private Long id;
    private String name;
    private String logoUrl;
    private String description;
    private String website;
    private String industry;
    private String employeeCount;
    private Integer foundedYear;
    private String mission;
    private String vision;
    private String location;
    private String address;
    private String email;
    private String phone;
    private String facebook;
    private String linkedin;
    private String twitter;
    private String instagram;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 