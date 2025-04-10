package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "applications")
@Getter
@Setter
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    Long id;

    @Column(length = 500)
    String coverLetter;

    @Enumerated(EnumType.STRING)
    ApplicationStatus status;

    String recruiterNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    Candidate candidate;
}
