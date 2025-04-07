package org.sportstogo.backend.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String email;
    private LocalDate dateCreated;
    public User(String username, String email, LocalDate dateCreated) {
        this.username = username;
        this.email = email;
        this.dateCreated = dateCreated;
    }
}
