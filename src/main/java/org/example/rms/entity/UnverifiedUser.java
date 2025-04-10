package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "unverified_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnverifiedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long userId;

    @Column(unique = true, nullable = false)
    String email;

    String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    Role role;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime userCreatedAt;

    String otp;

    @CreationTimestamp
    LocalDateTime otpCreatedAt;

    @CreationTimestamp
    LocalDateTime latestOtpInterval;

    int otpRequestCount;

    @PreUpdate
    public void preUpdate() {
        if(otp != null) {
            otpCreatedAt = LocalDateTime.now();
        }
    }

    @Version
    Long version;
}
