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
    public ResponseEntity<String> registerNewUser(@RequestBody User user) {
        this.userService.registerNewUser(user);
        return ResponseEntity.ok()
                .body("User registered successfully");
    }
    @DeleteMapping(path = "{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable("user_id")Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok()
                .body("User deleted successfully");
    }
    @PutMapping(path = "{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable("user_id")Long id,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String username) {
        userService.updateUser(id, email, username);
        return ResponseEntity.ok()
                .body("User updated successfully");
    }
    @GetMapping(path = "recent")
    public List<User> getRecentUsers() {
        return this.userService.getUsersRegisteredLastWeek();
    }

}
