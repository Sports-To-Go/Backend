package org.sportstogo.backend.Repository;

import org.sportstogo.backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and manipulating user data in the database.
 * This interface extends JpaRepository and provides custom query methods for finding users by their email and username.
 * JpaRepository provides basic CRUD operations (save, delete, findById, etc.) out of the box.
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
