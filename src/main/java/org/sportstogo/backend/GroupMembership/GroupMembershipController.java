package org.sportstogo.backend.GroupMembership;


import org.sportstogo.backend.Group.GroupMembershipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/groupMembership")
public class GroupMembershipController {
    private GroupMembershipService groupMembershipService;
    @GetMapping
    public List<GroupMembership> get_group_memberships() {
        return groupMembershipService.get_group_memberships();
    }
    @PostMapping
    public void add_group_membership(@RequestBody GroupMembership groupMembership) {
        groupMembershipService.add_group_membership(groupMembership);
    }
    @PutMapping(path = "{groupMembershipId}")
    public void update_group_membership(@PathVariable("groupMembershipId") GroupMembershipId groupMembershipId,
                                        @RequestParam(required = true) Role role) {
        groupMembershipService.update_group_membership(groupMembershipId, role);
    }
    @DeleteMapping(path = "{groupMembershipId}")
    public void delete_group_membership(@PathVariable("groupMembershipId") GroupMembershipId groupMembershipId) {
        groupMembershipService.delete_group_membership(groupMembershipId);
    }
}
