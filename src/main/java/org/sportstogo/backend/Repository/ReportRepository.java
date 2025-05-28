package org.sportstogo.backend.Repository;

import jakarta.transaction.Transactional;
import org.sportstogo.backend.Models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Report r WHERE r.targetId = :targetId")
    void deleteByTargetId(String targetId);
}
