package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.*;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.JoinRequest;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Repository.GroupMembershipRepository;
import org.sportstogo.backend.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "social")
@AllArgsConstructor
public class SocialController {

    private final GroupService groupService;
    private final GroupMembershipService groupMembershipService;
    private final JoinRequestService joinRequestService;
    private final MessageService messageService;
    private final ChatService chatService;
    private final UserService userService;

    @GetMapping(path="/groups")
    public ResponseEntity<List<GroupDataDTO>> getGroups(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        List<GroupDataDTO> groupData = groupService.getGroupData(uid);

        return ResponseEntity.ok(groupData);
    }

    @GetMapping(path="/groups/{groupId}")
    public ResponseEntity<GroupDataDTO> getGroup(Authentication authentication, @PathVariable Long groupId) {
        String uid = (String) authentication.getPrincipal();
        if(!groupMembershipService.isMemberOfGroup(uid, groupId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        GroupDataDTO groupData = groupService.getGroupDataById(groupId);
        if (groupData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(groupData);
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
        chatService.createSystemMessage(createdGroup.getId(), uid, "GROUP_CREATED", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @GetMapping("/group/{groupId}/messages")
    public ResponseEntity<List<MessageDTO>> getGroupMessages(@PathVariable Long groupId,
                                                             @RequestParam(name = "before") String beforeTimestamp,
                                                             Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        if(!groupMembershipService.isMemberOfGroup(uid, groupId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LocalDateTime before = LocalDateTime.parse(beforeTimestamp);
        List<MessageDTO> messages = messageService.getMessagesForGroup(groupId, before);
        return ResponseEntity.ok(messages);
    }


    @PostMapping("/join-request/{groupId}")
    public ResponseEntity<JoinRequest> createJoinRequest(@PathVariable Long groupId, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        JoinRequest joinRequest = joinRequestService.addJoinRequest(uid, groupId);
        if (joinRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Map<String, Object> meta = new HashMap<>();
        meta.put("uid", uid);
        meta.put("displayName", FirebaseTokenService.getDisplayNameFromUid(uid));
        chatService.createSystemMessage(groupId, uid, "JOIN_REQUEST", meta);
        return ResponseEntity.status(HttpStatus.CREATED).body(joinRequest);
    }

    @PutMapping("group/{groupId}/theme/{theme}")
    public ResponseEntity<Boolean> changeTheme(@PathVariable Long groupId,  @PathVariable String theme, Authentication authentication ) {
        String uid = (String) authentication.getPrincipal();
        Map<String,Object> map = new HashMap<>();
        map.put("themeName", theme);
        chatService.createSystemMessage(groupId, uid, "THEME_CHANGED", map);
        return  new ResponseEntity<>(groupMembershipService.changeTheme(uid, groupId, theme), HttpStatus.OK);
    }

    @PutMapping("group/nickname")
    public ResponseEntity<Boolean> changeNickname(Authentication authentication, @RequestBody GroupMemberShortDTO groupMemberShortDTO) {
        String uid = (String) authentication.getPrincipal();
        Map<String,Object> map = new HashMap<>();
        map.put("changedByName",uid);
        map.put("uid", groupMemberShortDTO.getUid());
        map.put("nickname", groupMemberShortDTO.getNickname());
        chatService.createSystemMessage(groupMemberShortDTO.getGroupId(), uid, "NICKNAME_CHANGED", map);

        return new ResponseEntity<>(groupMembershipService.changeNickname(uid, groupMemberShortDTO), HttpStatus.OK);
    }

    @PutMapping("/join-requests/handle")
    public ResponseEntity<Void> handleJoinRequest(@RequestBody HandleJoinRequestDTO request,
                                                            Authentication authentication) {
        String uid = (String) authentication.getPrincipal();

        if (!groupMembershipService.hasPermissions(uid, request.getGroupId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        GroupMemberDTO groupMemberDTO = request.isAccepted() ? groupMembershipService.addGroupMember(request.getGroupId(), request.getId()) : null;
        joinRequestService.removeRequest(request.getGroupId(), request.getId());

        if(request.isAccepted()) {
            Map<String, Object> userMeta = new HashMap<>();

            assert groupMemberDTO != null;
            userMeta.put("uid", groupMemberDTO.getId());
            userMeta.put("displayName", groupMemberDTO.getDisplayName());
            userMeta.put("role", groupMemberDTO.getGroupRole().name());
            chatService.createSystemMessage(
                    request.getGroupId(),
                    uid,
                    "USER_JOINED",
                    userMeta
            );

            Map<String, Object> groupMeta = new HashMap<>();
            groupMeta.put("groupID", request.getGroupId());
            chatService.sendDirectSystemMessage(request.getId(), "JOINED_GROUP", groupMeta);
        }

        return ResponseEntity.ok(null);
    }

    @DeleteMapping(path="/group/{groupID}")
    public ResponseEntity<Boolean> deleteGroupMember(@PathVariable Long groupID, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        boolean removed = groupMembershipService.removeUserFromGroup(uid, groupID);
        chatService.createSystemMessage(groupID, uid, "GROUP_DELETED", null);
        if(removed) return ResponseEntity.ok(true);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/not-basic")
    public ResponseEntity<List<GroupPreviewDTO>> getGroupsWhereUserHasElevatedRole(@RequestParam String uid) {
        List<GroupPreviewDTO> groups = groupService.getGroupsWhereUserIsNotBasic(uid);
        return ResponseEntity.ok(groups);
    }
}
