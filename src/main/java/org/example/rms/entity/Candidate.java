package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor
public class Candidate {
    @Id
    Long id;

    @MapsId
    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    User user;

    String name;

    @Enumerated(EnumType.STRING)
    Gender gender;

    LocalDate dateOfBirth;

    String phone;

    String address;

    String cvUrl;

    String profilePictureUrl;

    String summary;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Application> applications;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Interview> interviews;
}
