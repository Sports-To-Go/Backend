package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating user data in the database.
 * This interface extends JpaRepository and provides custom query methods for finding users by their email and username.
 * JpaRepository provides basic CRUD operations (save, delete, findById, etc.) out of the box.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * This query searches the database for a user with the given email.
     *
     * @param email the email address of the user to search for
     * @return an Optional containing the user if found, otherwise empty
     */
    @Query("Select u from User u where u.email=?1")
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their username.
     * This query searches the database for a user with the given username.
     *
     * @param username the username of the user to search for
     * @return an Optional containing the user if found, otherwise empty
     */
    @Query("Select u from User u where u.username=?1")
    Optional<User> findByUsername(String username);
}
