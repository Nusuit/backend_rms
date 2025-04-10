package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "interviews")
@Getter
@Setter
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


    @Column(columnDefinition = "nvarchar2(255)")
    String note;
}





