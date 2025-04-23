package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends JpaRepository<User,Long> {

    @Query("Select u from User u where u.dateCreated >= ?1")
    List<User> findUsersRegisteredAfter(java.time.LocalDate date);
}
