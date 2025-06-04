package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Enums.PeriodType;
import org.sportstogo.backend.Models.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    List<Revenue> findByPeriodStartAndPeriodType(LocalDate periodStart, PeriodType periodType);
}
