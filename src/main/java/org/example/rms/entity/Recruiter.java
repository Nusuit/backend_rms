package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Entity
@Table(name = "recruiters")
@Getter
@Setter
@NoArgsConstructor
public class Recruiter {
    @Id
    Long id;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    @MapsId
    User user;

    String name;

    String phone;

    String logo;

    String companyName;

    String companyAddress;

    String companyDescription;

    String companyWebsite;

    @OneToMany(mappedBy = "recruiter")
    List<Job> jobs;

}
