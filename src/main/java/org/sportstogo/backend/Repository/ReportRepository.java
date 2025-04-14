package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
