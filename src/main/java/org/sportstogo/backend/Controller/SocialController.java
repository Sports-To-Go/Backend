package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.*;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.Image;
import org.sportstogo.backend.Models.JoinRequest;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "social")
@AllArgsConstructor
public class SocialController {

    private final GroupService groupService;
    private final GroupMembershipService groupMembershipService;
    private final JoinRequestService joinRequestService;
    private final MessageService messageService;
    private final ChatService chatService;
    private final ImageService imageService;
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

    @PostMapping(path = "/group", consumes = {"multipart/form-data"})
    public ResponseEntity<GroupDataDTO> createGroup(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication
    ) {
        String uid = (String) authentication.getPrincipal();

        Image newImage = null;
        try {
            if (image != null && !image.isEmpty()) {
                newImage = imageService.saveImage(image);  // all validation is done here
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        Group createdGroup = groupService.createGroup(name, description, uid, newImage);
        chatService.createSystemMessage(createdGroup.getId(), uid, "GROUP_CREATED", null);

        GroupDataDTO dto = groupService.getGroupDataById(createdGroup.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
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
        meta.put("displayName", userService.getUserByUid(uid).getDisplayName());
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
            userMeta.put("imageUrl", groupMemberDTO.getImageUrl());
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

    @PutMapping("/group/{groupID}/members/{targetUID}/role")
    public ResponseEntity<Void> changeRole(
            Authentication auth,
            @PathVariable Long groupID,
            @PathVariable String targetUID,
            @RequestBody Map<String, String> body) {
        String newRole = body.get("newRole");
        String callerUID = (String) auth.getPrincipal();

        groupMembershipService.changeRole(callerUID, targetUID, groupID, newRole);
        Map<String, Object> meta = new HashMap<>();
        meta.put("uid", targetUID);
        meta.put("newRole", newRole);
        meta.put("displayName", FirebaseTokenService.getDataFromUid(targetUID));

        chatService.createSystemMessage(groupID, callerUID, "ROLE_CHANGED", meta);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping(path="/group/{groupID}")
    public ResponseEntity<Boolean> deleteGroupMember(@PathVariable Long groupID, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();

        boolean removed = groupMembershipService.removeUserFromGroup(uid, groupID);
        if (!removed) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        Map<String, Object> meta = new HashMap<>();
        meta.put("displayName", userService.getUserByUid(uid).getDisplayName());
        chatService.createSystemMessage(groupID, uid, "USER_LEFT", meta);

        // Check if group is now empty
        if (groupMembershipService.countGroupMembers(groupID) == 0) {
            groupService.deleteGroup(groupID);
        }

        return ResponseEntity.ok(true);
    }


    @DeleteMapping("/group/{groupID}/members/{targetUID}")
    public ResponseEntity<Void> kickMember(
            Authentication auth,
            @PathVariable Long groupID,
            @PathVariable String targetUID) {
        String callerUID = (String) auth.getPrincipal();
        groupMembershipService.kickMember(callerUID, targetUID, groupID);
        chatService.createSystemMessage(groupID, callerUID, "USER_KICKED", Map.of(
                "uid", targetUID,
                "kickedBy", callerUID,
                "kickedByName", FirebaseTokenService.getDataFromUid(callerUID),
                "kickedName", FirebaseTokenService.getDataFromUid(targetUID)
        ));;
        return ResponseEntity.ok().build();
    }


    @GetMapping("/not-basic")
    public ResponseEntity<List<GroupPreviewDTO>> getGroupsWhereUserHasElevatedRole(@RequestParam String uid) {
        List<GroupPreviewDTO> groups = groupService.getGroupsWhereUserIsNotBasic(uid);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/images/upload")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("image") MultipartFile image,
            Authentication authentication) {

        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No image provided"));
        }

        if (image.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(Map.of("error", "Image too large (max 5MB)"));
        }

        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid image type"));
        }

        try {
            Image savedImage = imageService.saveImage(image);
            return ResponseEntity.ok(Map.of("imageUrl", savedImage.getUrl(), "imageId", savedImage.getId().toString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload image"));
        }
    }
}

