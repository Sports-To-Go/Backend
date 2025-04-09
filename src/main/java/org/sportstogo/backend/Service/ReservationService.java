package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Reservation;
import org.sportstogo.backend.Repository.ReservationRepository;
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

    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Transactional
    public void updateReservation(Long id, Reservation updatedReservation) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new IllegalArgumentException("Reservation with id " + id + " does not exist");
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
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation with id " + id + " does not exist");
        }
        reservationRepository.deleteById(id);
    }
}