package org.sportstogo.backend.Service;


import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.GroupJoinRequest;
import org.sportstogo.backend.Repository.GroupJoinRequestRepo;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.sportstogo.backend.idModels.GroupMembershipId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class GroupJoinRequestService {
    private GroupJoinRequestRepo groupJoinRequestRepo;
    private GroupRepository groupRepository;
    private UserRepository userRepository;
    public ResponseEntity<GroupJoinRequest> getGroupJoinRequestById(GroupMembershipId groupMembershipId) {
        var x = groupJoinRequestRepo.findById(groupMembershipId);
        if (x.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok().body(x.get());
    }

    public ResponseEntity<?> deleteGroupJoinRequestById(GroupMembershipId groupMembershipId) {
        if (!groupJoinRequestRepo.existsById(groupMembershipId)) {
            return ResponseEntity.notFound().build();
        }
        groupJoinRequestRepo.deleteById(groupMembershipId);
        return ResponseEntity.ok()
                .body("GroupJoinRequest has been deleted");
    }

    public ResponseEntity<?> addGroupJoinRequest(GroupJoinRequest groupJoinRequest) {

        if (groupJoinRequest.getRequestDateTime() == null){
            groupJoinRequest.setRequestDateTime(LocalDateTime.now());
        }
        if (!userRepository.existsById(groupJoinRequest.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else if (!groupRepository.existsById(groupJoinRequest.getGroupId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
        groupJoinRequestRepo.save(groupJoinRequest);
        return  ResponseEntity.ok().body("GroupJoinRequest has been added");
    }
}
