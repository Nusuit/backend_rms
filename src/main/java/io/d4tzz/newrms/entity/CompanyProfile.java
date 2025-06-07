package io.d4tzz.newrms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "company_profiles")
public class CompanyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String logoUrl; // URL to the stored logo
    @Column(length = 2048) // Increased length for longer text
    private String description;
    private String website;
    private String industry;
    private String employeeCount; // e.g., "1-50", "51-200", "201-500", "501-1000", "1000+"
    private Integer foundedYear;
    @Column(length = 1020)
    private String mission;
    @Column(length = 1020)
    private String vision;
    private String location; // City, Country
    private String address; // Street address
    private String email;
    private String phone;
    private String facebook; // e.g., username or page ID
    private String linkedin; // e.g., company name or ID for URL
    private String twitter; // e.g., username
    private String instagram; // e.g., username

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Default constructor
    public CompanyProfile() {
    }

    // You might want to add a constructor for easier object creation
    public CompanyProfile(String name, String description) {
        this.name = name;
        this.description = description;
    }
} 