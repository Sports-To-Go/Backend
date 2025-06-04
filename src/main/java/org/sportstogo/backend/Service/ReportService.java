package org.sportstogo.backend.Service;


import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Report;
import org.sportstogo.backend.Enums.ReportStatus;
import org.sportstogo.backend.Enums.ReportTargetType;
import org.sportstogo.backend.Repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {
    private ReportRepository reportRepository;

    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    public List<Report> getReportsByTargetIdAndType(String targetId, ReportTargetType targetType) {
        return reportRepository.findAll().stream()
                .filter(report -> report.getTargetId().equals(targetId))
                .filter(report -> report.getTargetType() == targetType)
                .collect(Collectors.toList());
    }

    public void addReport(Report report) {
        reportRepository.save(report);
    }

    @Transactional
    public void updateReport(Long id, String reviewedBy, LocalDate reviewedAt, ReportStatus status) {

        Optional<Report> optionalReport = reportRepository.findById(id);

        if (optionalReport.isEmpty()) {
            throw new IllegalArgumentException("Report with id " + id + " does not exist");
        }

        Report report = optionalReport.get();

        if (reviewedBy != null) {
            report.setReviewedBy(reviewedBy);
        }

        if (reviewedAt != null) {
            report.setReviewedAt(reviewedAt);
        }

        if (status != null) {
            report.setStatus(status);
        }

    }


    public void deleteByTargetId(String targetId) {
        reportRepository.deleteByTargetId(targetId);
    }
    public void deleteReport(Long id) {
        if  (!reportRepository.existsById(id)) {
            throw new IllegalArgumentException("Report with id " + id + " does not exist");
        }
        reportRepository.deleteById(id);
    }
}
