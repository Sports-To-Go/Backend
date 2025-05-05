package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="Users")
@Getter @Setter
public class User {
    @Id
    @Column(nullable=false, unique=true)
    private String uid;
    private String description;
    private boolean isAdmin = false;
}
