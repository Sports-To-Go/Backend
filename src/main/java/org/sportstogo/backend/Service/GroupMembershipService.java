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

@Service
@AllArgsConstructor
public class GroupMembershipService {

    private final GroupMembershipRepo groupMembershipRepo;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    public List<GroupMembership> getGroupMemberships() {
        return groupMembershipRepo.findAll();
    }

    public ResponseEntity<?> addGroupMembership(GroupMembership groupMembership) {
        groupMembership.setJoinTime(LocalDateTime.now());
        if (groupMembership.getRole() == null) {
            groupMembership.setRole(Role.member);
        }
        if (!userRepository.existsById(groupMembership.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else if (!groupRepository.existsById(groupMembership.getGroupId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
        groupMembershipRepo.save(groupMembership);
        return ResponseEntity.status(HttpStatus.CREATED).body("Group membership successfully added");
    }

    public ResponseEntity<?> updateGroupMembership(GroupMembershipId groupMembershipId, Role role) {
        Optional<GroupMembership> groupMembership = groupMembershipRepo.findById(groupMembershipId);
        if (groupMembership.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group membership not found");
        }
        groupMembership.get().setRole(role);
        return ResponseEntity.ok("Group membership successfully updated");
    }

    public ResponseEntity<?> deleteGroupMembership(GroupMembershipId groupMembershipId) {
        if (!groupMembershipRepo.existsById(groupMembershipId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group membership not found");
        }
        groupMembershipRepo.deleteById(groupMembershipId);
        return ResponseEntity.ok("Group membership successfully deleted");
    }
}
