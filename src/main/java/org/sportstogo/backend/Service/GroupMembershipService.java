package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.GroupMemberDTO;
import org.sportstogo.backend.Enums.Role;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.GroupMembershipRepository;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.MessageRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupMembershipService {

    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public boolean isMemberOfGroup(String uid, Long groupID) {
        return groupMembershipRepository.existsByUserIDAndGroupID(uid, groupID);
    }

    public boolean removeUserFromGroup(String uid, Long groupID) {
        GroupMembership membership = groupMembershipRepository.findByUserIDAndGroupID(uid, groupID);

        if (membership == null) return false;

        groupMembershipRepository.delete(membership);

        // Check if the group still has any members
        if (!groupMembershipRepository.existsByGroupID(groupID)) {
            // If no members remain, remove all associated messages and then delete the group
            messageRepository.deleteAllByGroupId(groupID);
            groupRepository.deleteById(groupID);
        }
        return true;
    }


    public boolean hasPermissions(String uid, Long groupID) {
        GroupMembership membership = groupMembershipRepository.findByUserIDAndGroupID(uid, groupID);
        return membership != null && membership.getRole() != Role.member;
    }

    public GroupMemberDTO addGroupMember(Long groupId, String id) {
        // Check for existing membership
        if (groupMembershipRepository.existsByUserIDAndGroupID(id, groupId)) {
            throw new IllegalStateException("User is already a member of the group.");
        }

        // Retrieve the group
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (!groupOptional.isPresent()) {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found.");
        }
        Group group = groupOptional.get();

        // Retrieve the user
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
        User user = userOptional.get();

        // Create a new GroupMembership with default role member
        GroupMembership newMembership = new GroupMembership();
        newMembership.setGroupID(group);
        newMembership.setUserID(user);
        newMembership.setRole(Role.member); // default role for new membership

        // Save the new membership
        GroupMembership savedMembership = groupMembershipRepository.save(newMembership);

        // Create a GroupMemberDTO from the saved membership
        GroupMemberDTO dto = new GroupMemberDTO();
        dto.setDisplayName(FirebaseTokenService.getDisplayNameFromUid(id));
        dto.setId(id);
        dto.setRole(savedMembership.getRole());

        return dto;
    }
}
