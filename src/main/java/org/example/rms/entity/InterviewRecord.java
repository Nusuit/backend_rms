package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "interviewees")
@Getter
@Setter
public class InterviewRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    Application application;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    InterviewSchedule schedule;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    InterviewStage stage;

    @Column(columnDefinition = "nvarchar2(255)")
    String note;

    boolean passed;
}





