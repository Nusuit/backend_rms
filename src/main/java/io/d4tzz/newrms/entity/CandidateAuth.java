package io.d4tzz.newrms.entity;

import io.d4tzz.newrms.entity.enums.OtpType;
import io.d4tzz.newrms.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "candidates_auth")
public class CandidateAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authId")
    private Long authId;

    private String email;

    private String password;

    @Column(columnDefinition = "varchar2(500)")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    OtpType otpType;

    private String otp;

    private LocalDateTime otpCreatedAt;

    private LocalDateTime latestOtpWindow;

    @Column(name = "otp_request_count", columnDefinition = "INT DEFAULT 0")
    private int otpAttempt;
}
