package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.GroupJoinRequest;
import org.sportstogo.backend.Service.GroupJoinRequestService;
import org.sportstogo.backend.idModels.GroupMembershipId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "groupJoinRequests")
@AllArgsConstructor
public class GroupJoinRequestController {
    private GroupJoinRequestService groupJoinRequestService;
    @GetMapping
    public ResponseEntity<GroupJoinRequest> getGroupJoinRequestById(@RequestBody GroupMembershipId groupMembershipId) {
        return groupJoinRequestService.getGroupJoinRequestById(groupMembershipId);
    }
    @DeleteMapping
    public ResponseEntity<?> deleteGroupJoinRequestById(@RequestBody GroupMembershipId groupMembershipId) {
        return groupJoinRequestService.deleteGroupJoinRequestById(groupMembershipId);
    }
    @PostMapping
    public ResponseEntity<?> addGroupJoinRequest(@RequestBody GroupJoinRequest groupJoinRequest) {
        return groupJoinRequestService.addGroupJoinRequest(groupJoinRequest);
    }
}
