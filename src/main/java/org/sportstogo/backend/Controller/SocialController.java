package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.ChatPreviewDTO;
import org.sportstogo.backend.DTOs.GroupCreationDTO;
import org.sportstogo.backend.DTOs.GroupDataDTO;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Service.GroupMembershipService;
import org.sportstogo.backend.Service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "social")
@AllArgsConstructor
public class SocialController {

    private final GroupService groupService;
    private final GroupMembershipService groupMembershipService;

    @GetMapping(path="/chat-previews")
    public ResponseEntity<List<ChatPreviewDTO>> getChatPreviews(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        List<ChatPreviewDTO> groupPreviews = groupService.getChatPreviews(uid);

        return ResponseEntity.ok(groupPreviews);
    }

    @GetMapping(path="/group/{groupID}")
    public ResponseEntity<GroupDataDTO> getGroup(@PathVariable Long groupID, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        boolean isMember = groupMembershipService.isMemberOfGroup(uid, groupID);
        if(!isMember) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        GroupDataDTO groupDataDTO = groupService.getGroupData(groupID);
        return ResponseEntity.ok(groupDataDTO);
    }

    @PostMapping(path="/group")
    public ResponseEntity<Group> createGroup(@RequestBody GroupCreationDTO groupCreationDTO, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        Group createdGroup = groupService.createGroup(groupCreationDTO, uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @DeleteMapping(path="/group/{groupID}")
    public ResponseEntity<Boolean> deleteGroupMember(@PathVariable Long groupID, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        boolean removed = groupMembershipService.removeUserFromGroup(uid, groupID);
        if(removed) return ResponseEntity.ok(true);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
