package io.d4tzz.newrms.entity;

import io.d4tzz.newrms.entity.enums.JobStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String requirement;

    private String benefit;

    private Long minSalary;

    private Long maxSalary;

    private String salaryCurrency;

    private LocalDate deadline;

    private String Location;

    private String note;

    @Enumerated(EnumType.STRING)
    JobStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    @OrderBy
    private Set<JobSkill> skills;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    @OrderBy
    private Set<JobStage> stages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_process_id")
    private RecruitmentProcess process;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @PostLoad
    private void onLoad() {
        this.status = computeStatus();
    }

    @PrePersist
    @PreUpdate
    private void preSave() {
        this.status = computeStatus();
    }

    private JobStatus computeStatus() {
        if (status != JobStatus.CANCELED) {
            if (LocalDate.now().isAfter(deadline)) {
                return JobStatus.CLOSED;
            } else {
                return JobStatus.OPEN;
            }
        }

        return JobStatus.CANCELED;
    }

}
