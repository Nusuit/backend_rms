package io.d4tzz.newrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "job_skill")
public class JobSkill {
//    @EmbeddedId
//    private JobSkillId id = new JobSkillId();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_skill_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private boolean required;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JobSkill jobSkill = (JobSkill) o;
        return required == jobSkill.required && Objects.equals(id, jobSkill.id) && Objects.equals(job, jobSkill.job) && Objects.equals(skill, jobSkill.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, job, skill, required);
    }
}
