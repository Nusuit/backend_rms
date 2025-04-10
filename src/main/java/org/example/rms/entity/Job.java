package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    Long id;

    String title;

    String description;

    String requirement;

    String benefit;

    String salary;

    LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    JobStatus status;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    Recruiter recruiter;

    @OneToMany(mappedBy = "job")
    List<Application> applications;

    @OneToMany(mappedBy = "job", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    Set<SkillOfJob> skills;

    @PostLoad
    void updateStatus() {
        if (deadline != null && Duration.between(deadline, LocalDateTime.now()).toSeconds() > 0) {
            status = JobStatus.CLOSED;
        }
    }
}
