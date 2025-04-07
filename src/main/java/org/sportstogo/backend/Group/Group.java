package org.sportstogo.backend.Group;


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
    private String name;
    private Long  createdBy;
    private LocalDate createdDate;
    public Group(String name, Long createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }
}
