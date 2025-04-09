package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.idModels.GroupMembershipId;
import org.sportstogo.backend.Enums.Role;
import org.sportstogo.backend.Service.GroupMembershipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/groupMemberships")
@AllArgsConstructor
public class GroupMembershipController {

    private final GroupMembershipService groupMembershipService;

    @GetMapping
    public List<GroupMembership> getGroupMemberships() {
        return groupMembershipService.getGroupMemberships();
    }

    @PostMapping
    public ResponseEntity<?> addGroupMembership(@RequestBody GroupMembership groupMembership) {
        return groupMembershipService.addGroupMembership(groupMembership);
    }

    @PutMapping
    public ResponseEntity<?> updateGroupMembership(
            @RequestBody GroupMembershipId groupMembershipId,
            @RequestParam Role role) {
        return groupMembershipService.updateGroupMembership(groupMembershipId, role);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteGroupMembership(@RequestBody GroupMembershipId groupMembershipId) {
        return groupMembershipService.deleteGroupMembership(groupMembershipId);
    }
}
