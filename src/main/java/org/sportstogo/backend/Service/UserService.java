package org.sportstogo.backend.Service;

import com.google.firebase.auth.UserRecord;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Controller.AdminController;
import org.sportstogo.backend.Exceptions.UserNotFoundException;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.Image;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.ImageRepository;
import org.sportstogo.backend.Repository.MessageRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.sportstogo.backend.Repository.GroupMembershipRepository;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final GroupMembershipRepository groupMembershipRepository;


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

    @Transactional
    public void deleteUserAndTransferOwnership(String uid) {
        Optional<User> optionalUser = userRepository.findById(uid);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User userToDelete = optionalUser.get();

        User undefinedUser = userRepository.findById("undefined")
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUid("undefined");
                    newUser.setDescription("Deleted User");
                    newUser.setDisplayName("Deleted User");
                    return userRepository.save(newUser);
                });

        messageRepository.updateUserReferences(uid, "undefined");

        groupRepository.updateCreatedBy(uid, "undefined");

        List<GroupMembership> memberships = groupMembershipRepository.findByUserID_Uid(uid);
        groupMembershipRepository.deleteAll(memberships);

        userRepository.delete(userToDelete);
    }



    public void saveUser(User user) {
        userRepository.save(user);
    }


    public boolean userExists(String uid) {
        return userRepository.existsById(uid);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }
}
