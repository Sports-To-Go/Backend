package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "bans")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String idUser;

    @Column(nullable = false)
    private LocalDate beginTime;

    @Column(nullable = false)
    private LocalDate endTime;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String bannedBy;
}
