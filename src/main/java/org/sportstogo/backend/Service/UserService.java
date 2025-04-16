package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class for managing user-related operations.
 * This class contains methods for registering, updating, and deleting users,
 * as well as validating user data such as email format and uniqueness of username and email.
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves a list of all users from the repository.
     *
     * @return a list of users
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Registers a new user after validating their email and username.
     * Checks if the email or username already exists in the database,
     * and validates the email format using a regular expression.
     *
     * @param user the user object to be registered
     * @return a ResponseEntity indicating the result of the operation
     */
    public ResponseEntity<?> registerNewUser(User user) {
        // Check if email or username already exists
        if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        } else if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Validate email format using regular expression
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(user.getEmail());
        if (!matcher.matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }

        // Set the current date for user creation and save the user
        user.setDateCreated(LocalDate.now());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User successfully registered");
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     * @return a ResponseEntity indicating the result of the operation
     */
    public ResponseEntity<?> deleteById(Long id) {
        // Check if the user exists before attempting deletion
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User successfully deleted");
    }

    /**
     * Updates the user information (email and/or username) by user ID.
     * If a new email or username is provided, it checks for uniqueness and valid format.
     *
     * @param id the ID of the user to be updated
     * @param email the new email (optional)
     * @param username the new username (optional)
     * @return a ResponseEntity indicating the result of the operation
     */
    @Transactional
    public ResponseEntity<?> updateUser(Long id, String email, String username) {
        // Retrieve the user by ID
        Optional<User> userOp = userRepository.findById(id);
        if (userOp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = userOp.get();

        // Update email if provided
        if (email != null) {
            if (this.userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }

            // Validate the new email format
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
            }

            user.setEmail(email);
        }

        // Update username if provided
        if (username != null) {
            if (this.userRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            user.setUsername(username);
        }

        return ResponseEntity.ok("User successfully updated");
    }
}
