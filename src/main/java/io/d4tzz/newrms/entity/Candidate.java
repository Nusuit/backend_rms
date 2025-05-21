package io.d4tzz.newrms.entity;

import io.d4tzz.newrms.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    CandidateAuth auth;

    String name;

    @Enumerated(EnumType.STRING)
    Gender gender;

    LocalDate dateOfBirth;

    String phone;

    String address;

    String cvUrl;

    String avatarUrl;
}
