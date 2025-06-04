package org.sportstogo.backend.Controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Reservation;
import org.sportstogo.backend.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
/**
 * REST Controller for managing Reservation entities
 * Provides endpoints for CRUD operations on reservations
 */
@RestController
@RequestMapping(path = "/reservations")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

    /**
     * @return a list of all reservations
     */
    @GetMapping
    public List<Reservation> getReservations() {
        return reservationService.getReservations();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable String userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<String> addReservation(@RequestBody Reservation reservation) {
        return reservationService.addReservation(reservation);
    }

    @PutMapping(path = "{reservation_id}")
    public ResponseEntity<String> updateReservation(@PathVariable("reservation_id") Long id,
                                                    @RequestBody Reservation reservation) {
        return reservationService.updateReservation(id, reservation);
    }

    @Transactional
    @DeleteMapping(path = "{reservation_id}")
    public ResponseEntity<String> deleteReservation(@PathVariable("reservation_id") Long id) {
        return reservationService.deleteReservation(id);
    }
}