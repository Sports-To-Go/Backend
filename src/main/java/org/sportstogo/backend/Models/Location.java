package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.DTOs.LocationDTO;
import org.sportstogo.backend.Enums.Sport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * address of the location
     */
    @Column(nullable = false)
    private String address;
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
     * the ID of the user who owns the location to the platform. cannot be null
     */
    @Column(nullable = false)
    private String createdBy;
    /**
     * a short description of the location
     */
    private String description;
    /**
     * the sport that can be practiced at the location. cannot be null
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Sport sport;
    /**
     * calendar ID
     */
    private String calendarId;
    /**
     * hourly rate for renting the location
     */
    private double hourlyRate;
    /**
     * opening time for the location
     */
    private LocalTime openingTime;
    /**
     * closing time for the location
     */
    private LocalTime closingTime;
    /**
     *
     */
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocationImage> images = new ArrayList<>();

    /**
     * the date when the location was added to the system
     */
    private LocalDate createdAt;
    /**
     * whether a location has been verified by an admin or not
     */
    private boolean verified;

    public Location(String name,String address, double longitude,double latitude, String createdBy,
                    String description,Sport sport, String calendarId, double hourlyRate,
                    LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createdBy = createdBy;
        this.description = description;
        this.sport = sport;
        this.calendarId = calendarId;
        this.hourlyRate = hourlyRate;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public static LocationDTO mapToDTO(Location location) {
        List<String> imageUrls = location.getImages() == null ? List.of() :
                location.getImages().stream()
                        .map(LocationImage::getImage)
                        .filter(Objects::nonNull)
                        .map(Image::getUrl)
                        .toList();

        return new LocationDTO(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getLongitude(),
                location.getLatitude(),
                location.getCreatedBy(),
                location.getDescription(),
                location.getSport(),
                location.getCalendarId(),
                location.getHourlyRate(),
                location.getOpeningTime(),
                location.getClosingTime(),
                location.isVerified(),
                imageUrls
        );
    }



}
