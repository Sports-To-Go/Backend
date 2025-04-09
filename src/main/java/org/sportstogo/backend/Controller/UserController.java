package org.sportstogo.backend.Controller;

import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return this.userService.getUsers();
    }

    @PostMapping
    public ResponseEntity<?> registerNewUser(@RequestBody User user) {
        return this.userService.registerNewUser(user);
    }

    @DeleteMapping(path = "{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") Long id) {
        return userService.deleteById(id);
    }

    @PutMapping(path = "{user_id}")
    public ResponseEntity<?> updateUser(@PathVariable("user_id") Long id,
                                        @RequestParam(required = false) String email,
                                        @RequestParam(required = false) String username) {
        return userService.updateUser(id, email, username);
    }
}
