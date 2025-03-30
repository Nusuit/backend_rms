package org.example.rms.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "interviews")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "application_id")
    Application application;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    InterviewSchedule schedule;

    String type;

    String interviewName;

    @Column(columnDefinition = "nvarchar2(255)")
    String notes;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

}





