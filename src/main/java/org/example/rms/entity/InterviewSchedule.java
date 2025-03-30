package org.example.rms.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "interview_schedules")
public class InterviewSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "schedule")
    List<Interview> interviews;
}
