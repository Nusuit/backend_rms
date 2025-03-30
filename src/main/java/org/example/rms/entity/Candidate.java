package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor
public class Candidate {
    @Id
    Long candidateId;

    @MapsId
    @OneToOne(cascade = { CascadeType.REMOVE })
    @JoinColumn(name = "user_id")
    User user;

    String name;

    @Enumerated(EnumType.STRING)
    UserGender gender;

    LocalDateTime dateOfBirth;

    String phone;

    String logoUrl;

    String summary;

    @OneToMany(mappedBy = "candidate")
    List<Application> applications;

    @OneToMany(mappedBy = "candidate")
    List<Interview> interviews;
}
