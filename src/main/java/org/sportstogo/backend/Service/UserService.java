package org.sportstogo.backend.Service;

import com.google.firebase.auth.UserRecord;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Exceptions.UserNotFoundException;
import org.sportstogo.backend.Models.Image;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.ImageRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public User getUserByUid(String uid) {
        return userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User with UID " + uid + " not found"));
    }

    public User createUser(String uid) {
        User user = new User();
        user.setUid(uid);
        user.setDescription("");
        user.setAdmin(false);
        UserRecord userRecord = FirebaseTokenService.getDataFromUid(uid);

        user.setDisplayName(userRecord.getDisplayName() );
        if (userRecord.getPhotoUrl() != null) {
            Image image = new Image();
            image.setUrl(userRecord.getPhotoUrl());
            user.setImage(imageRepository.save(image));
        }

        return userRepository.save(user);
    }

    public User updateUser(String uid, String description, Image newImage) {
        User user = getUserByUid(uid);

        if (description != null) {
            user.setDescription(description);
        }

        if (newImage != null) {
            user.setImage(newImage);
        }

        return userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


    public boolean userExists(String uid) {
        return userRepository.existsById(uid);
    }
}
