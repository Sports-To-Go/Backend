package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Reservation;
import org.sportstogo.backend.Repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {
    private ReservationRepository reservationRepository;

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public ResponseEntity<String> addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body("Reservation has been added");
    }

    @Transactional
    public ResponseEntity<String> updateReservation(Long id, Reservation updatedReservation) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Reservation with id " + id + " does not exist");
        }
        Reservation existingReservation = reservation.get();
        existingReservation.setLocationId(updatedReservation.getLocationId());
        existingReservation.setUserId(updatedReservation.getUserId());
        existingReservation.setGroupId(updatedReservation.getGroupId());
        existingReservation.setCalendarEventId(updatedReservation.getCalendarEventId());
        existingReservation.setStartTime(updatedReservation.getStartTime());
        existingReservation.setEndTime(updatedReservation.getEndTime());
        existingReservation.setTotalCost(updatedReservation.getTotalCost());
        existingReservation.setPaymentStatus(updatedReservation.getPaymentStatus());
        return ResponseEntity.status(HttpStatus.OK).body("Reservation has been updated");
    }

    public ResponseEntity<String> deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Reservation with id " + id + " does not exist");
        }
        reservationRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Reservation has been deleted");
    }
}