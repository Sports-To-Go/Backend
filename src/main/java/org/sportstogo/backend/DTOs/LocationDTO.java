package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sportstogo.backend.Enums.Sport;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Long id;
    private String name;
    private String address;
    private double longitude;
    private double latitude;
    private String createdBy;
    private String description;
    private Sport sport;
    private String calendarId;
    private double hourlyRate;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean verified;
    private List<String> imageUrls;
}
