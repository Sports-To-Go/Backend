package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Exceptions.UserNotFoundException;
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

    public User getUserByUid(String uid) {
        return userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User with UID " + uid + " not found"));
    }

    public User createUser(String uid) {
        User user = new User();
        user.setUid(uid);
        user.setDescription("");
        return userRepository.save(user);
    }
}
