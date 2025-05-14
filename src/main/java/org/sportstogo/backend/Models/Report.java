package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String reportedBy;

    @Column(nullable = false)
    private ReportTargetType targetType;

    @Column(nullable = false)
    private String targetId;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private ReportStatus status;

    private LocalDate createdAt;

    private String reviewedBy;

    private LocalDate reviewedAt;

    private LocalDate userNotifiedAt;

}
