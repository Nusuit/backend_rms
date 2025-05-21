package io.d4tzz.newrms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class JobStageId {
    @Column(name = "job_id")
    Long jobId;

    @Column(name = "stage_id")
    Long stageId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JobStageId that = (JobStageId) o;
        return Objects.equals(jobId, that.jobId) && Objects.equals(stageId, that.stageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, stageId);
    }
}
