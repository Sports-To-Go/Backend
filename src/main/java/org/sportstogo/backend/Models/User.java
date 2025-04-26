package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="Users")
@Getter @Setter
public class User {
    @Id private String uid;

    @Column(nullable = true)
    private String description;
}
