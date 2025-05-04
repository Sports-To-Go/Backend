package org.sportstogo.backend.Controller;

import org.sportstogo.backend.Models.Revenue;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.AdminService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
}
