package org.sportstogo.backend.Controller;

import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(path = "recent-users")
    public List<User> getRecentUsers() {
        return this.adminService.getUsersRegisteredLastWeek();
    }

    @GetMapping("/locations/count")
    public ResponseEntity<Long> getLocationCount() {
        return ResponseEntity.ok(adminService.getLocationCount());
    }

    @GetMapping("/reservations/count")
    public ResponseEntity<Long> getReservationCount() {
        long count = adminService.getReservationCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/recent-users/count")
    public long getUsersRegisteredLastWeekCount() {
        return adminService.getNumberOfUsersRegisteredInLastWeek();
    }
}
