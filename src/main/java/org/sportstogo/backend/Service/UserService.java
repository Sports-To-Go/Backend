package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Exceptions.UserNotFoundException;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;

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
        user.setAdmin(false);
        return userRepository.save(user);
    }

    public User updateUser(String uid, User updatedUserData) {
        User user = getUserByUid(uid); // Căutăm userul în DB

        if (updatedUserData.getDescription() != null) {
            user.setDescription(updatedUserData.getDescription());
        }

        // Dacă vei adăuga mai târziu și alte câmpuri editabile:
        // if (updatedUserData.getDisplayName() != null) {
        //     user.setDisplayName(updatedUserData.getDisplayName());
        // }

        return userRepository.save(user); // Salvăm modificările
    }
}
