package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="users")
@Getter @Setter
public class User {
    @Id
    @Column(nullable=false, unique=true)
    private String uid;

    @Column(nullable=false)
    private String displayName;

    private String description;
    private boolean isAdmin = false;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;
}
