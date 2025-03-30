package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    Role role;

    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}