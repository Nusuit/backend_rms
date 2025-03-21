package org.example.rms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor
public class Role {
    @Id
    private int roleId;

    private String roleName;
}
