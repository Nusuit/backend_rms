package org.example.rms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String cvPdfUrl;

    String coverLetter;

    @Enumerated(EnumType.ORDINAL)
    ApplicationStatus status;

    String recruiterNote;

    @ManyToOne
    @JoinColumn(name = "job_id")
    Job job;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    Candidate candidate;
}
