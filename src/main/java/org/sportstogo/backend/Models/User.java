package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a user entity in the system.
 * This class defines the structure of the User table in the database.
 * It contains user details like username, email, and the date the user was created.
 */
@Entity
@Table(name="Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class User {
    /**
     * Unique identifier for the user.
     * This is the primary key for the User table and is automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The username chosen by the user.
     * This field is non-nullable.
     */
    private String username;

    /**
     * The email of the user.
     * This field must be unique and cannot be null.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The date when the user was created.
     * This field is non-nullable.
     */
    @Column(nullable = false)
    private LocalDate dateCreated;
}
