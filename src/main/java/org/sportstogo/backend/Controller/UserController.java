package org.sportstogo.backend.Controller;

import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for managing user-related operations.
 * Provides endpoints to get users, register new users, delete users, and update user details.
 */
@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a new {@link UserController} with the provided {@link UserService}.
     *
     * @param userService the service used to handle user operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetches the list of all users.
     *
     * @return a list of all users
     */
    @GetMapping
    public List<User> getUsers() {
        return this.userService.getUsers();
    }

    /**
     * Registers a new user in the system.
     * Performs validation checks and adds the user if no conflicts are found (e.g., duplicate email or username).
     *
     * @param user the {@link User} object containing the user details
     * @return a response indicating whether the user was successfully registered or not
     */
    @PostMapping
    public ResponseEntity<?> registerNewUser(@RequestBody User user) {
        return this.userService.registerNewUser(user);
    }

    /**
     * Deletes a user from the system by their user ID.
     *
     * @param id the ID of the user to delete
     * @return a response indicating whether the user was successfully deleted or not
     */
    @DeleteMapping(path = "{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") Long id) {
        return userService.deleteById(id);
    }

    /**
     * Updates the details (email or username) of an existing user.
     * Only the email and username are updatable, and the request may provide one or both parameters.
     *
     * @param id       the ID of the user to update
     * @param email    the new email for the user (optional)
     * @param username the new username for the user (optional)
     * @return a response indicating whether the user was successfully updated or not
     */
    @PutMapping(path = "{user_id}")
    public ResponseEntity<?> updateUser(@PathVariable("user_id") Long id,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) String username) {
        return userService.updateUser(id, email, username);
    }
}
