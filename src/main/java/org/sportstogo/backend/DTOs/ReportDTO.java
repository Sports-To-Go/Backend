package org.sportstogo.backend.DTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ReportDTO {
    private final String reportedBy;
    private final String reason;
    private final LocalDate createdAt;
}