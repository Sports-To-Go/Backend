// src/main/java/org/sportstogo/backend/Models/Revenue.java
package org.sportstogo.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "revenues")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;


    public enum PeriodType {
        DAILY, MONTHLY, ANNUAL
    }
}
