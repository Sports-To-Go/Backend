package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "LocationRatings")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LocationRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private double score;

    @Column(length = 1000)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    private int reservationId;
    private int locationId;
    private String ratedBy;

    public LocationRating(double score, String comment, LocalDate createdAt,
                          int reservationId, int locationId, String ratedBy) {
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
        this.reservationId = reservationId;
        this.locationId = locationId;
        this.ratedBy = ratedBy;
    }
}
