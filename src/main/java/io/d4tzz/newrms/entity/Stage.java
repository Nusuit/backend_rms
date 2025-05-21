package io.d4tzz.newrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stages")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stage_id")
    private Long id;

    private String name;

    @Column(name = "stage_order")
    private int order;

//    @ManyToOne
//    @JoinColumn(name = "industry_id", nullable = false)
//    private Industry industry;
}
