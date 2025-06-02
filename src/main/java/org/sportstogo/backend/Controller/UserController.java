package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Exceptions.UserNotFoundException;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.FirebaseTokenService;
import org.sportstogo.backend.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(path="/profile")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        try {
            return ResponseEntity.ok(userService.getUserByUid(uid));
        } catch (UserNotFoundException e) {
            User createdUser = userService.createUser(uid);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }
    }

    @PostMapping(path="/profile")
    public ResponseEntity<User> createUser(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        System.out.println(uid);
        User createdUser = userService.createUser(uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping(path="/profile")
    public ResponseEntity<User> updateUserProfile(
            Authentication authentication,
            @RequestBody User updatedUserData) {
        String uid = (String) authentication.getPrincipal();
        User updatedUser = userService.updateUser(uid, updatedUserData);
        return ResponseEntity.ok(updatedUser);
    }

}
