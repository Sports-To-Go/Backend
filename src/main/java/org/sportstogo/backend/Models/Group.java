package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entity representing a user-defined group within the system.
 * A group has a unique ID, a name, and a creation date.
 */
@Entity
@Table(name = "groups")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Group {

    /**
     * Unique identifier for the group. Auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Name of the group. Cannot be null.
     */
    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private Long  createdBy;
    /**
     * The date when the group was created. Cannot be null.
     */
  
    @Column(nullable = false)
    private LocalDate createdDate;
}
