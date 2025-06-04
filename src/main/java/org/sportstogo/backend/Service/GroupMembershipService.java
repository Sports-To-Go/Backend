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
        return true;
    }

    public void kickMember(String callerUID, String targetUID, Long groupID) {
        GroupMembership caller = groupMembershipRepository.findByUserIDAndGroupID(callerUID, groupID);
        GroupMembership target = groupMembershipRepository.findByUserIDAndGroupID(targetUID, groupID);

        if (caller == null || target == null)
            throw new IllegalArgumentException("Caller or target not in group");

        if (callerUID.equals(targetUID))
            throw new IllegalArgumentException("Cannot kick yourself");

        if (caller.getGroupRole().ordinal() <= target.getGroupRole().ordinal())
            throw new IllegalArgumentException("Insufficient role to kick this user");

        groupMembershipRepository.delete(target);
    }

    public void changeRole(String callerUID, String targetUID, Long groupID, String newRoleStr) {
        GroupMembership caller = groupMembershipRepository.findByUserIDAndGroupID(callerUID, groupID);
        GroupMembership target = groupMembershipRepository.findByUserIDAndGroupID(targetUID, groupID);

        if (caller == null || target == null)
            throw new IllegalArgumentException("Caller or target not in group");

        GroupRole newRole = GroupRole.valueOf(newRoleStr);
        if (caller.getGroupRole() != GroupRole.admin)
            throw new IllegalArgumentException("Only admin can change roles");

        if (newRole == GroupRole.admin)
            throw new IllegalArgumentException("Cannot promote to admin");

        target.setGroupRole(newRole);
        groupMembershipRepository.save(target);
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
        if (groupOptional.isEmpty()) {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found.");
        }
        Group group = groupOptional.get();

        // Retrieve the user
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
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

        return savedMembership.toDTO();
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

    public int countGroupMembers(Long groupId) {
        return groupMembershipRepository.findByGroupID(groupId).size(); // or use count query
    }
}
