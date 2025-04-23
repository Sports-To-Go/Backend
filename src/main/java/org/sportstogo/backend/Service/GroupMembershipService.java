package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.idModels.GroupMembershipId;
import org.sportstogo.backend.Enums.Role;
import org.sportstogo.backend.Repository.GroupMembershipRepo;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for handling business logic related to group memberships.
 * Provides methods to get, add, update, and delete group memberships.
 */
@Service
@AllArgsConstructor
public class GroupMembershipService {

    private final GroupMembershipRepo groupMembershipRepo;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all group memberships.
     *
     * @return a list of all group memberships
     */
    public List<GroupMembership> getGroupMemberships() {
        return groupMembershipRepo.findAll();
    }

    /**
     * Adds a new group membership.
     * This method checks if the user and group exist before adding the membership.
     * If the role is not provided, it will default to 'member'.
     *
     * @param groupMembership the group membership object to be added
     * @return a response entity indicating the result of the operation
     */
    public ResponseEntity<?> addGroupMembership(GroupMembership groupMembership) {
        groupMembership.setJoinTime(LocalDateTime.now());
        if (groupMembership.getRole() == null) {
            groupMembership.setRole(Role.member); // Default role if none provided
        }
        if (!userRepository.existsById(groupMembership.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else if (!groupRepository.existsById(groupMembership.getGroupId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
        groupMembershipRepo.save(groupMembership);
        return ResponseEntity.status(HttpStatus.CREATED).body("Group membership successfully added");
    }

    /**
     * Updates an existing group membership by changing the user's role in the group.
     *
     * @param groupMembershipId the composite ID of the group membership (userId + groupId)
     * @param role the new role to assign to the user in the group
     * @return a response entity indicating the result of the operation
     */
    public ResponseEntity<?> updateGroupMembership(GroupMembershipId groupMembershipId, Role role) {
        Optional<GroupMembership> groupMembership = groupMembershipRepo.findById(groupMembershipId);
        if (groupMembership.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group membership not found");
        }
        groupMembership.get().setRole(role);
        return ResponseEntity.ok("Group membership successfully updated");
    }

    /**
     * Deletes a group membership.
     * This removes the association between a user and a group.
     *
     * @param groupMembershipId the composite ID of the group membership to be deleted (userId + groupId)
     * @return a response entity indicating the result of the operation
     */
    public ResponseEntity<?> deleteGroupMembership(GroupMembershipId groupMembershipId) {
        if (!groupMembershipRepo.existsById(groupMembershipId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group membership not found");
        }
        groupMembershipRepo.deleteById(groupMembershipId);
        return ResponseEntity.ok("Group membership successfully deleted");
    }
}
