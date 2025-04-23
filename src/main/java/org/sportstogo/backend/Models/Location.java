package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="Locations")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column( nullable = false)
    private double longitude;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private Long createdBy;
    private String calendarId;
    private double hourlyRate;
    private LocalDate createdAt;
    private boolean verified;

    public Location(String name, double longitude,double latitude, Long createdBy, String calendarId, double hourlyRate) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createdBy = createdBy;
        this.calendarId = calendarId;
        this.hourlyRate = hourlyRate;
    }

}
