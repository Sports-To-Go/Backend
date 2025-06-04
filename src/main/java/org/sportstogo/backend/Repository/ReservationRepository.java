package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.locationId = ?1")
    List<Reservation> findByLocationId(Long locationId);

    @Query("SELECT r FROM Reservation r WHERE r.userId = ?1")
    List<Reservation> findByUserId(String userId);

    @Query("SELECT r FROM Reservation r WHERE r.groupId = ?1")
    List<Reservation> findByGroupId(Long groupId);

    @Query("SELECT r FROM Reservation r WHERE r.calendarEventId = ?1")
    List<Reservation> findByCalendarEventId(String calendarEventId);
}