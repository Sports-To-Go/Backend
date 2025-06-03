package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.GroupMemberDTO;
import org.sportstogo.backend.DTOs.GroupMemberShortDTO;
import org.sportstogo.backend.Enums.GroupRole;
import org.sportstogo.backend.Enums.Theme;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupMembershipService {

    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final JoinRequestRepository joinRequestRepository;

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
            joinRequestRepository.deleteAllByGroupId(groupID);
            groupRepository.deleteById(groupID);
        }
        return true;
    }


    public boolean hasPermissions(String uid, Long groupID) {
        GroupMembership membership = groupMembershipRepository.findByUserIDAndGroupID(uid, groupID);
        return membership != null && membership.getGroupRole() != GroupRole.member;
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
        newMembership.setGroupRole(GroupRole.member); // default role for new membership

        // Save the new membership
        GroupMembership savedMembership = groupMembershipRepository.save(newMembership);

        // Create a GroupMemberDTO from the saved membership
        GroupMemberDTO dto = new GroupMemberDTO();
        dto.setDisplayName(FirebaseTokenService.getDisplayNameFromUid(id));
        dto.setId(id);
        dto.setGroupRole(savedMembership.getGroupRole());

        return dto;
    }

    public boolean changeTheme(String uid, Long groupId, String theme) {
        if (isMemberOfGroup(uid, groupId)) {
            groupRepository.updateGroupTheme(groupId, Theme.valueOf(theme));
            return true;
        } else return false;
    }

    public boolean changeNickname(String uid,GroupMemberShortDTO groupMemberShortDTO) {
        if (isMemberOfGroup(uid, groupMemberShortDTO.getGroupId())) {
            groupMembershipRepository.updateNickname(groupMemberShortDTO.getUid(),
                    groupMemberShortDTO.getGroupId(),
                    groupMemberShortDTO.getNickname());
        } else return false;
        return false;
    }
}
