package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_verification")
@Getter
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long userId;

    @Column(unique = true, nullable = false)
    String email;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime userCreatedAt;

    String otp;

    LocalDateTime otpCreatedAt;

    LocalDateTime firstOtpInterval;

    int otpRequestCount;


}
