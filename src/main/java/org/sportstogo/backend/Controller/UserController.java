package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Image;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.ImageService;
import org.sportstogo.backend.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping(path="/profile")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getUserByUid(uid));
    }

    @PostMapping(path="/profile")
    public ResponseEntity<User> createUser(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        if (userService.userExists(uid)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User createdUser = userService.createUser(uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping(path = "/profile", consumes = {"multipart/form-data"})
    public ResponseEntity<User> updateUser(
            Authentication authentication,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        String uid = (String) authentication.getPrincipal();

        Image newImage = null;
        try {
            if (image != null && !image.isEmpty()) {
                newImage = imageService.saveImage(image);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        User updatedUser = userService.updateUser(uid, description, newImage);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/profile/picture")
    public ResponseEntity<Void> removeProfilePicture(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();

        User user = userService.getUserByUid(uid);
        Image image = user.getImage();

        if (image != null) {
            user.setImage(null);
            userService.saveUser(user);
            imageService.deleteImageEntity(image);
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        userService.deleteUserAndTransferOwnership(uid);
        return ResponseEntity.noContent().build();
    }
}
