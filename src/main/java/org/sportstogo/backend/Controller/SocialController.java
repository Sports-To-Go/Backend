package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.*;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.JoinRequest;
import org.sportstogo.backend.Service.GroupMembershipService;
import org.sportstogo.backend.Service.GroupService;
import org.sportstogo.backend.Service.JoinRequestService;
import org.sportstogo.backend.Service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "social")
@AllArgsConstructor
public class SocialController {

    private final GroupService groupService;
    private final GroupMembershipService groupMembershipService;
    private final JoinRequestService joinRequestService;
    private final MessageService messageService;

    @GetMapping(path="/groups")
    public ResponseEntity<List<GroupDataDTO>> getGroups(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        List<GroupDataDTO> groupData = groupService.getGroupData(uid);

        return ResponseEntity.ok(groupData);
    }

    @GetMapping(path="/chat-previews")
    public ResponseEntity<List<GroupPreviewDTO>> getChatPreviews(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        List<GroupPreviewDTO> groupPreviews = groupService.getChatPreviews(uid);

        return ResponseEntity.ok(groupPreviews);
    }

    @GetMapping(path="/recommended-groups")
    public ResponseEntity<List<GroupPreviewDTO>> getRecommendedGroups(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        List<GroupPreviewDTO> groupPreviews = groupService.getGroupRecommendations(uid);

        return ResponseEntity.ok(groupPreviews);
    }

    @PostMapping(path="/group")
    public ResponseEntity<Group> createGroup(@RequestBody GroupCreationDTO groupCreationDTO, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        Group createdGroup = groupService.createGroup(groupCreationDTO, uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @GetMapping("/group/{groupId}/messages")
    public ResponseEntity<List<MessageDTO>> getGroupMessages(@PathVariable Long groupId,
                                                             @RequestParam(name = "before") String beforeTimestamp,
                                                             Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        // Optional: check membership before fetching messages
        LocalDateTime before = LocalDateTime.parse(beforeTimestamp);
        List<MessageDTO> messages = messageService.getMessagesForGroup(groupId, before);
        return ResponseEntity.ok(messages);
    }


    @PostMapping("/join-request/{groupId}")
    public ResponseEntity<JoinRequest> createJoinRequest(@PathVariable Long groupId, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        JoinRequest joinRequest = joinRequestService.addJoinRequest(uid, groupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(joinRequest);
    }

    @PostMapping("/join-requests/handle")
    public ResponseEntity<GroupMemberDTO> handleJoinRequest(@RequestBody HandleJoinRequestDTO request,
                                                            Authentication authentication) {
        // Extract the UID of the current (admin) user from the authentication token.
        String uid = (String) authentication.getPrincipal();

        // Check if the current user has the permissions to manage members of the group.
        // We assume hasPermissions(String uid, Long groupId) checks if adminUid is either admin or co_admin.
        if (!groupMembershipService.hasPermissions(uid, request.getGroupId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        GroupMemberDTO groupMemberDTO = request.isAccepted() ? groupMembershipService.addGroupMember(request.getGroupId(), request.getId()) : null;
        joinRequestService.removeRequest(request.getGroupId(), request.getId());

        return ResponseEntity.ok(groupMemberDTO);
    }

    @DeleteMapping(path="/group/{groupID}")
    public ResponseEntity<Boolean> deleteGroupMember(@PathVariable Long groupID, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        boolean removed = groupMembershipService.removeUserFromGroup(uid, groupID);
        if(removed) return ResponseEntity.ok(true);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
