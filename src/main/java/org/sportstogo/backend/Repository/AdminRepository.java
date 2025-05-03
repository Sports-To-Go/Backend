package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AdminRepository extends JpaRepository<User,Long> {

    @Query("Select u from User u where u.dateCreated >= ?1")
    List<User> findUsersRegisteredAfter(java.time.LocalDate date);

    @Query("SELECT COUNT(l) FROM Location l")
    long countAllLocations();

    @Query("SELECT COUNT(r) FROM Reservation r")
    long countReservations();

    @Query("SELECT COUNT(u) FROM User u WHERE u.dateCreated >= :date")
    long countUsersRegisteredAfter(@Param("date") LocalDate date);

}
