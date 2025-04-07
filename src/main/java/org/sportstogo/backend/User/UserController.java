package org.sportstogo.backend.User;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<User> get_users() {
        return this.userService.get_users();
    }

    @PostMapping
    public void register_new_user(@RequestBody User user) {
        this.userService.register_new_user(user);
    }
    @DeleteMapping(path = "{user_id}")
    public void delete_user(@PathVariable("user_id")Long id) {
        userService.delete_by_id(id);
    }
    @PutMapping(path = "{user_id}")
    public void update_user(@PathVariable("user_id")Long id,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) String username) {
        userService.update_user(id, email, username);
    }
}
