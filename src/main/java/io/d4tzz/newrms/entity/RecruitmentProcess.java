package io.d4tzz.newrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "recruitment_processes")
public class RecruitmentProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_process_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "process")
    @OrderBy("order ASC")
    private Set<Stage> stages;
}
