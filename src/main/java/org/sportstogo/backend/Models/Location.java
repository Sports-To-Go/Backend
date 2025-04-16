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
    /**
     * Unique identifier for the location. Auto-generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * name of the location. cannot be null
     */
    @Column(nullable = false)
    private String name;
    /**
     * longitude of the location. cannot be null
     */
    @Column( nullable = false)
    private double longitude;
    /**
     * latitude of the location. cannot be null
     */
    @Column(nullable = false)
    private double latitude;
    /**
     * the ID of the user who owns the location to the platform. cannot be nullable
     */
    @Column(nullable = false)
    private Long createdBy;
    /**
     * calendar ID
     */
    private String calendarId;
    /**
     * hourly rate for renting the location
     */
    private double hourlyRate;
    /**
     * the date when the location was added to the system
     */
    private LocalDate createdAt;
    /**
     * whether a location has been verified by an admin or not
     */
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
