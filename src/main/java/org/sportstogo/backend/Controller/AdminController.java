package org.sportstogo.backend.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.NameDTO;
import org.sportstogo.backend.DTOs.ReportDTO;

import org.sportstogo.backend.Enums.ReportStatus;
import org.sportstogo.backend.Enums.ReportTargetType;

import org.sportstogo.backend.DTOs.ReportInfoDTO;
import org.sportstogo.backend.Models.*;
import org.sportstogo.backend.Service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ReportService reportService;
    private final BanService banService;
    private final BugService bugService;
    private final LocationService locationService;
    private final UserService userService;

    @GetMapping(path = "recent-users")
    public ResponseEntity<List<User>> getRecentUsers(Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<User> users = this.adminService.getUsersRegisteredLastWeek();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/locations/count")
    public ResponseEntity<Long> getLocationCount(Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        long locationCount = adminService.getLocationCount();
        return ResponseEntity.ok(locationCount);
    }

    @GetMapping("/user/count")
    public ResponseEntity<Long> getUsersCount(Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        long locationCount = adminService.getUsersCount();
        return ResponseEntity.ok(locationCount);
    }

    @GetMapping("/reservations/count")
    public ResponseEntity<Long> getReservationCount(Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        long reservationCount = adminService.getReservationCount();
        return ResponseEntity.ok(reservationCount);
    }

    @GetMapping("/recent-users/count")
    public ResponseEntity<Long> getUsersRegisteredLastWeekCount(Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        long count = adminService.getNumberOfUsersRegisteredInLastWeek();
        return ResponseEntity.ok(count);
    }


    @PostMapping("/revenue")
    public ResponseEntity<Revenue> addRevenue(
            @RequestBody Revenue revenue,
            Authentication authentication
    ) {
        String uid = (String) authentication.getPrincipal();

        if (!adminService.isAdmin(uid)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Revenue saved = adminService.saveRevenue(revenue);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<Revenue>> getMonthlyRevenue(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication
    ) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        List<Revenue> revenues = adminService.getMonthlyRevenue(from, to);
        return revenues.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(revenues);
    }

    @GetMapping("/revenue/annual")
    public ResponseEntity<List<Revenue>> getAnnualRevenue(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication
    ) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        List<Revenue> revenues = adminService.getAnnualRevenue(from, to);
        return revenues.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(revenues);
    }


    //reports

    /**
     * @return returns all reports
     */
    @GetMapping(path = "reports")
    public ResponseEntity<List<Report>> getAllReports(Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        return ResponseEntity.ok(reportService.getReports());
    }

    @GetMapping(path = "/reports/targetUser/{targetId}")
    public ResponseEntity<List<Report>> getReportsForTargetUser(@PathVariable String targetId, Authentication authentication) {
        String authenticatedUid = (String) authentication.getPrincipal();

        if (!authenticatedUid.equals(targetId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        }

        List<Report> reports = reportService.getReportsByTargetIdAndType(targetId, ReportTargetType.User);
        if (reports.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(reports);
    }

    @GetMapping(path = "reports/info")
    public ResponseEntity<List<ReportInfoDTO>> getReportInfo(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();

        if (!adminService.isAdmin(uid)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Map<String, ReportInfoDTO> reportMap = new HashMap<>();

        reportService.getReports().forEach(report -> {
            String id = report.getTargetId();
            ReportTargetType type = report.getTargetType(); // get as String from enum

            String key = id + "-" + type;

            reportMap.compute(key, (k, existing) -> {
                if (existing == null) {
                    return new ReportInfoDTO(id, 1, type);
                } else {
                    existing.setReports(existing.getReports() + 1);
                    return existing;
                }
            });
        });

        List<ReportInfoDTO> response = new ArrayList<>(reportMap.values());

        return ResponseEntity.ok(response);
    }


    /**
     * @param report_type type of reports wanted to be returned
     * @return a list of all reports of the specified type
     * @throws IllegalArgumentException if the report type specified is invalid
     */
    @GetMapping(path = "reports/{report_type}")
    public ResponseEntity<List<Report>> getReportsByType(@PathVariable("report_type") String report_type, Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        if (Arrays.stream(ReportTargetType.values()).noneMatch(t -> t.name().equalsIgnoreCase(report_type.toLowerCase()))) {
            throw new IllegalArgumentException("Invalid report type");
        }
        return ResponseEntity.ok(
                reportService
                        .getReports()
                        .stream()
                        .filter(report -> report
                                .getTargetType()
                                .toString()
                                .equalsIgnoreCase(report_type.toLowerCase()))
                        .collect(Collectors.toList()));
    }

    @GetMapping(path = "reports/{report_type}/{target_id}/messages")
    public ResponseEntity<List<ReportDTO>> getAllByTypeAndTargetId(
            @PathVariable("report_type") String reportType,
            @PathVariable("target_id") String targetId,
            Authentication authentication
    ) {
        String uid = (String) authentication.getPrincipal();
        if (!adminService.isAdmin(uid)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // Validate reportType against enum names (ignore case)
        boolean validType = Arrays.stream(ReportTargetType.values())
                .anyMatch(t -> t.name().equalsIgnoreCase(reportType));
        if (!validType) {
            throw new IllegalArgumentException("Invalid report type: " + reportType);
        }

        // Filter, map to DTO, collect
        List<ReportDTO> dtos = reportService.getReports().stream()
                .filter(r -> r.getTargetType().name().equalsIgnoreCase(reportType))
                .filter(r -> r.getTargetId().equals(targetId))
                .map(r -> new ReportDTO(
                        r.getReportedBy(),
                        r.getReason(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());

        // Always return 200 OK with list (empty if no matches)
        return ResponseEntity.ok(dtos);
    }

    /**
     * @param report the Report object to be added; must contain reportedBy, targetType, targetId, reason, and status
     * @return a ResponseEntity containing a success message with the newly created report's ID
     */
    @PostMapping(path = "reports")
    public ResponseEntity<String> addReports(@RequestBody Report report, Authentication authentication) {
        report.setCreatedAt(LocalDate.now());
        reportService.addReport(report);
        return ResponseEntity.ok()
                .body("Report with id " + report.getId() + " added successful");
    }

    /**
     * @param id         the ID of the report to update
     * @param reviewedBy optional ID of the admin reviewing the report
     * @param reviewedAt optional date when the report was reviewed
     * @param status     optional new status of the report
     * @return a ResponseEntity containing a success message if the update was successful
     */
    @PutMapping(path = "reports/{report_id}")
    public ResponseEntity<String> updateReport(
            @PathVariable("report_id") Long id,
            @RequestParam(required = false) String reviewedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewedAt,
            @RequestParam(required = false) ReportStatus status,
            Authentication authentication
    ) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


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
    public ResponseEntity<String> deleteReport(@PathVariable("report_id") Long id, Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        reportService.deleteReport(id);
        return ResponseEntity.ok()
                .body("Report with id " + id + " deleted successfully");
    }

    @Transactional
    @DeleteMapping(path = "reports/{target_type}/{target_id}")
    public ResponseEntity<String> deleteReportByTargetIdAndType(
            @PathVariable("target_type") String targetType,
            @PathVariable("target_id") String target_id,
            Authentication authentication
    ) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        if (Arrays.stream(ReportTargetType.values()).noneMatch(t -> t.name().equalsIgnoreCase(targetType.toLowerCase()))) {
            return ResponseEntity.badRequest().body("Invalid target type");
        }

        List<Report> reportList = reportService.getReports().stream()
                .filter(report -> report.getTargetType().name().equalsIgnoreCase(targetType.toLowerCase()))
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
     *
     * @return a list of all Ban records
     */
    @GetMapping(path = "bans")
    public ResponseEntity<List<Ban>> getBans(Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        return ResponseEntity.ok(banService.getBans());
    }

    /**
     * Creates a new ban. Automatically sets the start time to today and computes the end date based on the duration.
     *
     * @param ban the Ban object to be created
     * @return a ResponseEntity with a success message
     */
    @PostMapping(path = "bans")
    public ResponseEntity<String> addBan(@RequestBody Ban ban, Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        ban.setBeginTime(LocalDate.now());

        banService.addBan(ban);
        return ResponseEntity.ok()
                .body("Ban with id " + ban.getId() + " added successfully");
    }

    /**
     * Updates an existing ban's duration and/or reason.
     *
     * @param id       the ID of the ban to update
     * @param duration optional new duration in days
     * @param reason   optional new reason for the ban
     * @return a ResponseEntity with a success message
     */
    @PutMapping(path = "bans/{ban_id}")
    public ResponseEntity<String> updateBan(
            @PathVariable("ban_id") Long id,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) String reason,
            Authentication authentication
    ) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        banService.updateBan(id, duration, reason);
        return ResponseEntity.ok()
                .body("Ban with id " + id + " updated successfully");
    }

    /**
     * Deletes a ban by its ID.
     *
     * @param id the ID of the ban to delete
     * @return a ResponseEntity with a success message
     */
    @DeleteMapping(path = "bans/{ban_id}")
    public ResponseEntity<String> deleteBan(@PathVariable("ban_id") Long id, Authentication authentication) {

        String uid = (String) authentication.getPrincipal();

        boolean isAdmin = adminService.isAdmin(uid);

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        banService.deleteBan(id);
        return ResponseEntity.ok()
                .body("Ban with id " + id + " deleted successfully");
    }

    //Bugs

    @PostMapping("/bug")
    public ResponseEntity<Bug> addBug(@RequestBody Bug bug) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bugService.addBug(bug));
    }

    @PutMapping("/bug/{id}/resolve")
    public ResponseEntity<Bug> resolveBug(@PathVariable Long id) {
        return ResponseEntity.ok(bugService.resolveBug(id));
    }

    @GetMapping("/bug/getAll")
    public ResponseEntity<List<Bug>> getUnresolvedBugs(@RequestParam(defaultValue = "false") boolean status) {
        if (status) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
        List<Bug> bugs = bugService.getUnresolvedBugs();
        return bugs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(bugs);
    }

    @GetMapping("/{id}/{type}/name")
    public ResponseEntity<NameDTO> getDisplayName(
            @PathVariable("id") String id,
            @PathVariable("type") ReportTargetType type,
            Authentication authentication
    ){
        String requester = (String) authentication.getPrincipal();
        if (!adminService.isAdmin(requester)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try{
            String name = "";
            String imageUrl = null;
            if(type == ReportTargetType.User){
                name = userService.getUserById(id).getDisplayName();
            }else if(type == ReportTargetType.Location){
                Location location = locationService.getLocationById(Long.parseLong(id));
                name = location.getName();
                if (!location.getImages().isEmpty()) {
                    imageUrl = location.getImages().getFirst().getImage().getUrl();
                }
            }
            return ResponseEntity.ok(new NameDTO(name, imageUrl));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
