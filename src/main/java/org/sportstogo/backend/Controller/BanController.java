package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Ban;
import org.sportstogo.backend.Service.BanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsible for managing user bans.
 */
@RestController
@RequestMapping(path = "/administration/bans")
@AllArgsConstructor
public class BanController {

    private final BanService banService;

    /**
     * Retrieves all bans in the system.
     * @return a list of all Ban records
     */
    @GetMapping
    public List<Ban> getBans() {
        return banService.getBans();
    }

    /**
     * Creates a new ban. Automatically sets the start time to today and computes the end date based on the duration.
     * @param ban the Ban object to be created
     * @return a ResponseEntity with a success message
     */
    @PostMapping
    public ResponseEntity<String> addBan(@RequestBody Ban ban) {
        ban.setBeginTime(LocalDate.now());
        ban.setEndTime(LocalDate.now().plusDays(ban.getDuration()));

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
    @PutMapping(path = "{ban_id}")
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
    @DeleteMapping(path = "{ban_id}")
    public ResponseEntity<String> deleteBan(@PathVariable("ban_id") Long id) {
        banService.deleteBan(id);
        return ResponseEntity.ok()
                .body("Ban with id " + id + " deleted successfully");
    }
}
