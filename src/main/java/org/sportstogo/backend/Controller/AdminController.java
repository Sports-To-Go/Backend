package org.sportstogo.backend.Controller;

import jakarta.transaction.Transactional;
import org.sportstogo.backend.Models.*;
import org.sportstogo.backend.Service.AdminService;
import org.sportstogo.backend.Service.BanService;
import org.sportstogo.backend.Service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "admin")
public class AdminController {

    private final AdminService adminService;
    private final ReportService reportService;
    private final BanService banService;

    public AdminController(AdminService adminService, ReportService reportService, BanService banService) {
        this.adminService = adminService;
        this.reportService = reportService;
        this.banService = banService;
    }

    @GetMapping(path = "recent-users")
    public ResponseEntity<List<User>> getRecentUsers() {
        List<User> users = this.adminService.getUsersRegisteredLastWeek();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/locations/count")
    public ResponseEntity<Long> getLocationCount() {
        long locationCount = adminService.getLocationCount();
        return ResponseEntity.ok(locationCount);
    }

    @GetMapping("/reservations/count")
    public ResponseEntity<Long> getReservationCount() {
        long reservationCount = adminService.getReservationCount();
        return ResponseEntity.ok(reservationCount);
    }

    @GetMapping("/recent-users/count")
    public ResponseEntity<Long> getUsersRegisteredLastWeekCount() {
        long count = adminService.getNumberOfUsersRegisteredInLastWeek();
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<List<Revenue>> getMonthlyRevenue(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<Revenue> revenues = adminService.getMonthlyRevenue(from, to);
        return revenues.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(revenues);
    }

    /**
     * Venituri anuale între două date (inclusiv).
     * Ex: /admin/revenue/annual?from=2020-01-01&to=2025-01-01
     */
    @GetMapping("/revenue/annual")
    public ResponseEntity<List<Revenue>> getAnnualRevenue(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<Revenue> revenues = adminService.getAnnualRevenue(from, to);
        return revenues.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(revenues);
    }




    //reports
    /**
     *
     * @return returns all reports
     */
    @GetMapping(path = "reports")
    public List<Report> getAllReports() { return reportService.getReports(); }

    /**
     *
     * @param report_type type of reports wanted to be returned
     * @return a list of all reports of the specified type
     * @throws IllegalArgumentException if the report type specified is invalid
     */
    @GetMapping(path = "reports/{report_type}")
    public List<Report> getReportsByType(@PathVariable("report_type") String report_type) {
        if (Arrays.stream(ReportTargetType.values()).noneMatch(t -> t.name().equalsIgnoreCase(report_type.toLowerCase()))) {
            throw new IllegalArgumentException("Invalid report type");
        }
        return reportService.getReports().stream().filter(report -> report.getTargetType().toString().equalsIgnoreCase(report_type.toLowerCase())).collect(Collectors.toList());
    }

    @GetMapping(path = "reports/{report_type}/{target_id}/messages")
    public List<String> getAllReasonsByTypeAndTargetId(@PathVariable("report_type") String report_type, @PathVariable("target_id") String target_id) {

        if (Arrays.stream(ReportTargetType.values()).noneMatch(t -> t.name().equalsIgnoreCase(report_type.toLowerCase()))) {
            throw new IllegalArgumentException("Invalid report type");
        }

        List<String> reasons = reportService.getReports().stream()
                .filter(report -> report.getTargetType().name().equalsIgnoreCase(report_type.toLowerCase()))
                .filter(report -> report.getTargetId().equals(target_id))
                .map(Report::getReason)
                .collect(Collectors.toList());

        if (reasons.isEmpty()) {
            return null;
        }

        return reasons;
    }

    /**
     * @param report the Report object to be added; must contain reportedBy, targetType, targetId, reason, and status
     * @return a ResponseEntity containing a success message with the newly created report's ID
     */
    @PostMapping(path = "reports")
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
    @PutMapping(path = "reports/{report_id}")
    public ResponseEntity<String> updateReport(@PathVariable("report_id") Long id,
                                               @RequestParam(required = false) String reviewedBy,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewedAt,
                                               @RequestParam(required = false) ReportStatus status) {
        reportService.updateReport(id, reviewedBy, reviewedAt, status);
        return ResponseEntity.ok()
                .body("Report with id " + id + " updated successfuly");
    }

    /**
     * @param id the ID of the report to delete
     * @return a ResponseEntity containing a success message if the deletion was successful
     */
    @Transactional
    @DeleteMapping(path = "reports/{report_id}")
    public ResponseEntity<String> deleteReport(@PathVariable("report_id") Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok()
                .body("Report with id " + id + " deleted successfully");
    }

    @Transactional
    @DeleteMapping(path = "reports/{target_type}/{target_id}")
    public ResponseEntity<String> deleteReportByTargetIdAndType(@PathVariable("target_type") String targetType, @PathVariable("target_id") String target_id) {

        if (Arrays.stream(ReportTargetType.values()).noneMatch(t -> t.name().equalsIgnoreCase(targetType))) {
            return ResponseEntity.badRequest().body("Invalid target type");
        }



        List<Report> reportList = reportService.getReports().stream()
                .filter(report -> report.getTargetType().name().equalsIgnoreCase(targetType))
                .filter(report -> report.getTargetId().equals(target_id)).toList();

        if (reportList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        for (Report report : reportList) {
            reportService.deleteReport(report.getId());
        }

        return ResponseEntity.ok("Reports for user id " + target_id + " deleted successfully");
    }




    //bans
    /**
     * Retrieves all bans in the system.
     * @return a list of all Ban records
     */
    @GetMapping(path = "bans")
    public List<Ban> getBans() {
        return banService.getBans();
    }

    /**
     * Creates a new ban. Automatically sets the start time to today and computes the end date based on the duration.
     * @param ban the Ban object to be created
     * @return a ResponseEntity with a success message
     */
    @PostMapping(path = "bans")
    public ResponseEntity<String> addBan(@RequestBody Ban ban) {
        ban.setBeginTime(LocalDate.now());

        banService.addBan(ban);
        return ResponseEntity.ok()
                .body("Ban with id " + ban.getId() + " added successfully");
    }

    /**
     * Updates an existing ban's duration and/or reason.
     * @param id the ID of the ban to update
     * @param duration optional new duration in days
     * @param reason optional new reason for the ban
     * @return a ResponseEntity with a success message
     */
    @PutMapping(path = "bans/{ban_id}")
    public ResponseEntity<String> updateBan(
            @PathVariable("ban_id") Long id,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) String reason) {
        banService.updateBan(id, duration, reason);
        return ResponseEntity.ok()
                .body("Ban with id " + id + " updated successfully");
    }

    /**
     * Deletes a ban by its ID.
     * @param id the ID of the ban to delete
     * @return a ResponseEntity with a success message
     */
    @DeleteMapping(path = "bans/{ban_id}")
    public ResponseEntity<String> deleteBan(@PathVariable("ban_id") Long id) {
        banService.deleteBan(id);
        return ResponseEntity.ok()
                .body("Ban with id " + id + " deleted successfully");
    }

}
