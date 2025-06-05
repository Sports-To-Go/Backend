package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    private Long locationId;
    private String locationName;
    private String locationImage;
    private String userId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private String paymentStatus;
    private Double totalCost;
}