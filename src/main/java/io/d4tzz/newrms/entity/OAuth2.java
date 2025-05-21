package io.d4tzz.newrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "oauth2")
public class OAuth2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String providerName;

    private String providerUserId;

    @OneToOne
    @JoinColumn(name = "candidate_auth_id")
    private CandidateAuth candidateAuth;

    private String code;

    private LocalDateTime codeCreatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
