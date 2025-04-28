//package org.sportstogo.backend.Controller;
//
//import lombok.AllArgsConstructor;
//import org.sportstogo.backend.Models.GroupMembership;
//import org.sportstogo.backend.idModels.GroupMembershipId;
//import org.sportstogo.backend.Enums.Role;
//import org.sportstogo.backend.Service.GroupMembershipService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * Controller for managing group memberships in the application.
// * Provides endpoints to add, update, retrieve, and delete group memberships.
// */
//@RestController
//@RequestMapping(path = "/groupMemberships")
//@AllArgsConstructor
//public class GroupMembershipController {
//
//    private final GroupMembershipService groupMembershipService;
//
//    /**
//     * Retrieves all group memberships.
//     *
//     * @return a list of all group memberships
//     */
//    @GetMapping
//    public List<GroupMembership> getGroupMemberships() {
//        return groupMembershipService.getGroupMemberships();
//    }
//
//    /**
//     * Adds a new group membership.
//     * The membership will link a user to a group with a specified role.
//     *
//     * @param groupMembership the group membership to be added
//     * @return a response entity indicating the result of the operation
//     */
//    @PostMapping
//    public ResponseEntity<?> addGroupMembership(@RequestBody GroupMembership groupMembership) {
//        return groupMembershipService.addGroupMembership(groupMembership);
//    }
//
//    /**
//     * Updates an existing group membership by changing the user's role in the group.
//     *
//     * @param groupMembershipId the composite ID of the group membership (userId + groupId)
//     * @param role the new role to be assigned to the user in the group
//     * @return a response entity indicating the result of the operation
//     */
//    @PutMapping
//    public ResponseEntity<?> updateGroupMembership(
//            @RequestBody GroupMembershipId groupMembershipId,
//            @RequestParam Role role) {
//        return groupMembershipService.updateGroupMembership(groupMembershipId, role);
//    }
//
//    /**
//     * Deletes an existing group membership.
//     *
//     * @param groupMembershipId the composite ID of the group membership to be deleted (userId + groupId)
//     * @return a response entity indicating the result of the operation
//     */
//    @DeleteMapping
//    public ResponseEntity<?> deleteGroupMembership(@RequestBody GroupMembershipId groupMembershipId) {
//        return groupMembershipService.deleteGroupMembership(groupMembershipId);
//    }
//}
