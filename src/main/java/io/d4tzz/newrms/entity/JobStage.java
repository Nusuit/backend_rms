package io.d4tzz.newrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job_stage")
public class JobStage {
//    @Id
//    private JobStageId id = new JobStageId();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_stage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("stageId")
    @JoinColumn(name = "stage_id")
    private Stage stage;

    @Column(name = "job_stage_order")
    private Integer order;
}
