package org.sportstogo.backend.Controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Reservation;
import org.sportstogo.backend.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

    @GetMapping
    public List<Reservation> getReservations() {
        return reservationService.getReservations();
    }

    @PostMapping
    public ResponseEntity<String> addReservation(@RequestBody Reservation reservation) {
        reservationService.addReservation(reservation);
        return ResponseEntity.ok()
                .body("Reservation added successfully");
    }

    @PutMapping(path = "{reservation_id}")
    public ResponseEntity<String> updateReservation(@PathVariable("reservation_id") Long id,
                                                    @RequestBody Reservation reservation) {
        reservationService.updateReservation(id, reservation);
        return ResponseEntity.ok()
                .body("Reservation updated successfully");
    }

    @Transactional
    @DeleteMapping(path = "{reservation_id}")
    public ResponseEntity<String> deleteReservation(@PathVariable("reservation_id") Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok()
                .body("Reservation deleted successfully");
    }
}