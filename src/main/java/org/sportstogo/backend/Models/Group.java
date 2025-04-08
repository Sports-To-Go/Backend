package org.sportstogo.backend.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "groups")
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long  createdBy;
    @Column(nullable = false)
    private LocalDate createdDate;
    public Group(String name, Long createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }
}
