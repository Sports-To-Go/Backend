package org.sportstogo.backend.Controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Report;
import org.sportstogo.backend.Models.ReportStatus;
import org.sportstogo.backend.Models.ReportTargetType;
import org.sportstogo.backend.Service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/administration/reports")
@AllArgsConstructor
public class ReportController {

    private ReportService reportService;
    /**
     *
     * @return returns all reports
     */
    @GetMapping
    public List<Report> getAllReports() { return reportService.getReports(); }

    /**
     *
     * @param report_type type of reports wanted to be returned
     * @return a list of all reports of the specified type
     * @throws IllegalArgumentException if the report type specified is invalid
     */
    @GetMapping(path = "{report_type}")
    public List<Report> getTypedReports(@PathVariable("report_type") String report_type) {
        if (Arrays.stream(ReportTargetType.values()).noneMatch(t -> t.name().equals(report_type))) {
            throw new IllegalArgumentException("Invalid report type");
        }
        return reportService.getReports().stream().filter(report -> report.getTargetType().toString().equals(report_type)).collect(Collectors.toList());
    }

    /**
     * @param report the Report object to be added; must contain reportedBy, targetType, targetId, reason, and status
     * @return a ResponseEntity containing a success message with the newly created report's ID
     */
    @PostMapping
    public ResponseEntity<String> addReports(@RequestBody Report report) {
        report.setCreatedAt(LocalDate.now());
        reportService.addReport(report);
        return ResponseEntity.ok()
                .body("Report with id " + report.getId() + " added successful");
    }

    /**
     * @param id the ID of the report to update
     * @param reviewedBy optional ID of the admin reviewing the report
     * @param reviewedAt optional date when the report was reviewed
     * @param status optional new status of the report
     * @return a ResponseEntity containing a success message if the update was successful
     */
    @PutMapping(path = "{report_id}")
    public ResponseEntity<String> updateReport(@PathVariable("report_id") Long id,
                                               @RequestParam(required = false) Long reviewedBy,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewedAt,
                                               @RequestParam(required = false) ReportStatus status) {
        reportService.updateReport(id, reviewedBy, reviewedAt, status);
        return ResponseEntity.ok()
                .body("Report with id " + id + " updated successful");
    }

    /**
     * @param id the ID of the report to delete
     * @return a ResponseEntity containing a success message if the deletion was successful
     */
    @Transactional
    @DeleteMapping(path = "{report_id}")
    public ResponseEntity<String> deleteReport(@PathVariable("report_id") Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok()
                .body("Report with id " + id + " deleted successful");
    }

}
