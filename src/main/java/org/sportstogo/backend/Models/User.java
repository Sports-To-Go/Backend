package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="Users")
@Getter @Setter
public class User {
    @Id private String uid;
    private String description;
}
