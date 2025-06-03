package io.d4tzz.newrms.entity;

import io.d4tzz.newrms.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recruiters_auth")
public class RecruiterAuth {
    @Id
    // Thay đổi GenerationType.IDENTITY thành SEQUENCE để có thể chỉ định ID trong mock data
    // và kiểm soát sequence rõ ràng hơn.
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recruiter_auth_seq")
    @SequenceGenerator(name = "recruiter_auth_seq", sequenceName = "RECRUITERS_AUTH_SEQ", allocationSize = 1)
    @Column(name = "recruiter_auth_id")
    private Long id;

    private String username;

    private String password;

    @Column(columnDefinition = "varchar2(500)")
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}

