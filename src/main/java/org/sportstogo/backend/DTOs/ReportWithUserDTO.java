package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReportWithUserDTO {
    private Long id;
    private String reason;
    private LocalDate createdAt;
    private String reportedBy;
    private String targetId;
    private String targetType;
    private String reporterImageUrl;
}
