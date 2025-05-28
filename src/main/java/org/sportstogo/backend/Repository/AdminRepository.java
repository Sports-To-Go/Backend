package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Models.Revenue;
import org.sportstogo.backend.Models.Revenue.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AdminRepository extends JpaRepository<User, String> {


    @Query(
            value = "SELECT u.uid, u.description " +
                    "FROM users u " +
                    "WHERE u.date_created >= :date",
            nativeQuery = true
    )
    List<User> findUsersRegisteredAfterNative(@Param("date") LocalDate date);

    @Query("SELECT COUNT(l) FROM Location l")
    long countAllLocations();

    @Query("SELECT COUNT(r) FROM Reservation r")
    long countReservations();

    @Query("SELECT COUNT(u) from User u")
    long countUsers();

    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM users u " +
                    "WHERE u.date_created >= :date",
            nativeQuery = true
    )
    long countUsersRegisteredAfterNative(@Param("date") LocalDate date);



    @Query("""
      SELECT r 
      FROM Revenue r
      WHERE r.periodType = :type
        AND r.periodStart BETWEEN :from AND :to
      ORDER BY r.periodStart
      """)
    List<Revenue> findRevenueByPeriodTypeAndPeriodStartBetween(
            @Param("type") PeriodType type,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
