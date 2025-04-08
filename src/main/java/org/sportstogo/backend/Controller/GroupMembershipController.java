package org.sportstogo.backend.Controller;


import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.GroupMembershipId;
import org.sportstogo.backend.Service.GroupMembershipService;
import org.sportstogo.backend.Models.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/groupMemberships")
@AllArgsConstructor
public class GroupMembershipController {
    private GroupMembershipService groupMembershipService;
    @GetMapping
    public List<GroupMembership> getGroupMemberships() {
        return groupMembershipService.getGroupMemberships();
    }
    @PostMapping
    public ResponseEntity<String> addGroupMembership(@RequestBody GroupMembership groupMembership) {
        groupMembershipService.add_group_membership(groupMembership);
        return  ResponseEntity.ok()
                .body("Group Membership added successfully");
    }
    @PutMapping(path = "{groupMembershipId}")
    public ResponseEntity<String> updateGroupMembership(@PathVariable("groupMembershipId") GroupMembershipId groupMembershipId,
                                        @RequestParam(required = true) Role role) {
        groupMembershipService.updateGroupMembership(groupMembershipId, role);
        return   ResponseEntity.ok()
                .body("Group Membership updated successfully");
    }
    @DeleteMapping(path = "{groupMembershipId}")
    public ResponseEntity<String> deleteGroupMembership(@PathVariable("groupMembershipId") GroupMembershipId groupMembershipId) {
        groupMembershipService.deleteGroupMembership(groupMembershipId);
        return  ResponseEntity.ok()
                .body("Group Membership deleted successfully");
    }
}
