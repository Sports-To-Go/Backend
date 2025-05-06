//package org.sportstogo.backend.Controller;
//
//import lombok.AllArgsConstructor;
//import org.sportstogo.backend.Models.GroupJoinRequest;
//import org.sportstogo.backend.Service.GroupJoinRequestService;
//import org.sportstogo.backend.idModels.GroupMemberID;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Controller for managing group join requests.
// */
//@RestController
//@RequestMapping(path = "/groupJoinRequests")
//@AllArgsConstructor
//public class GroupJoinRequestController {
//
//    private final GroupJoinRequestService groupJoinRequestService;
//
//    /**
//     * Get a group join request by its ID.
//     *
//     * @param userId the ID of the user
//     * @param groupId the ID of the group
//     * @return the group join request if found
//     */
//    @GetMapping("/{userId}/{groupId}")
//    public ResponseEntity<GroupJoinRequest> getGroupJoinRequestById(
//            @PathVariable Long userId, @PathVariable Long groupId) {
//        GroupMemberID groupMembershipId = new GroupMemberID(userId, groupId);
//        return groupJoinRequestService.getGroupJoinRequestById(groupMembershipId);
//    }
//
//    /**
//     * Delete a group join request by its ID.
//     *
//     * @param userId the ID of the user
//     * @param groupId the ID of the group
//     * @return response indicating success or failure
//     */
//    @DeleteMapping("/{userId}/{groupId}")
//    public ResponseEntity<?> deleteGroupJoinRequestById(
//            @PathVariable Long userId, @PathVariable Long groupId) {
//        GroupMemberID groupMembershipId = new GroupMemberID(userId, groupId);
//        return groupJoinRequestService.deleteGroupJoinRequestById(groupMembershipId);
//    }
//
//    /**
//     * Add a new group join request.
//     *
//     * @param groupJoinRequest the group join request to add
//     * @return response indicating the result of the operation
//     */
//    @PostMapping
//    public ResponseEntity<?> addGroupJoinRequest(@RequestBody GroupJoinRequest groupJoinRequest) {
//        return groupJoinRequestService.addGroupJoinRequest(groupJoinRequest);
//    }
//}
