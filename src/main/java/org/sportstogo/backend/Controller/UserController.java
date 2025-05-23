package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
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
        User user = userService.getUserByUid(uid);

        return ResponseEntity.ok(user);
    }

    @PostMapping(path="/profile")
    public ResponseEntity<User> createUser(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        User createdUser = userService.createUser(uid);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
