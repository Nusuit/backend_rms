package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InterviewStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String stageName;
}
