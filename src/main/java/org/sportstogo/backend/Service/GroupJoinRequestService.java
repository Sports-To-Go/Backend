//package org.sportstogo.backend.Service;
//
//import lombok.AllArgsConstructor;
//import org.sportstogo.backend.Models.GroupJoinRequest;
//import org.sportstogo.backend.Repository.GroupJoinRequestRepo;
//import org.sportstogo.backend.Repository.GroupRepository;
//import org.sportstogo.backend.Repository.UserRepository;
//import org.sportstogo.backend.idModels.GroupMembershipId;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//@AllArgsConstructor
//public class GroupJoinRequestService {
//
//    private final GroupJoinRequestRepo groupJoinRequestRepo;
//    private final GroupRepository groupRepository;
//    private final UserRepository userRepository;
//
//    /**
//     * Retrieve a group join request by its ID.
//     *
//     * @param groupMembershipId the ID of the group join request
//     * @return response with the group join request
//     */
//    public ResponseEntity<GroupJoinRequest> getGroupJoinRequestById(GroupMembershipId groupMembershipId) {
//        var request = groupJoinRequestRepo.findById(groupMembershipId);
//        return request.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
//
//    /**
//     * Delete a group join request by its ID.
//     *
//     * @param groupMembershipId the ID of the group join request
//     * @return response indicating success or failure
//     */
//    public ResponseEntity<?> deleteGroupJoinRequestById(GroupMembershipId groupMembershipId) {
//        if (!groupJoinRequestRepo.existsById(groupMembershipId)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group Join Request not found");
//        }
//        groupJoinRequestRepo.deleteById(groupMembershipId);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Group Join Request has been deleted");
//    }
//
//    /**
//     * Add a new group join request.
//     *
//     * @param groupJoinRequest the group join request to add
//     * @return response indicating the result of the operation
//     */
//    public ResponseEntity<?> addGroupJoinRequest(GroupJoinRequest groupJoinRequest) {
//        if (groupJoinRequest.getRequestDateTime() == null) {
//            groupJoinRequest.setRequestDateTime(LocalDateTime.now());
//        }
//
//        if (!userRepository.existsById(groupJoinRequest.getUserId())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//
//        if (!groupRepository.existsById(groupJoinRequest.getGroupId())) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
//        }
//
//        groupJoinRequestRepo.save(groupJoinRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Group Join Request has been successfully added");
//    }
//}
