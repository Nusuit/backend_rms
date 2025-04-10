package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Entity
@Getter
@Setter
public class SkillOfJob {
    @Embeddable
    static class JobSkillId {
        @Column(name  = "job_id")
        Long jobId;

        @Column(name = "skill_id")
        Long skillId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JobSkillId that = (JobSkillId) o;
            return jobId.equals(that.jobId) && skillId.equals(that.skillId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(jobId, skillId);
        }
    }

    @EmbeddedId
    JobSkillId sojId = new JobSkillId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    Skill skill;

    boolean required;
}
