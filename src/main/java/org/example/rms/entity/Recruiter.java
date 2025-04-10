package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "recruiters")
@Getter
@Setter
@NoArgsConstructor
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    @MapsId
    User user;

    String name;

    String description;

    String profilePictureUrl;

    @OneToMany(mappedBy = "recruiter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Job> jobs;

}
