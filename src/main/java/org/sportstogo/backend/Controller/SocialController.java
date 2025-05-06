package org.sportstogo.backend.Controller;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.ChatPreviewDTO;
import org.sportstogo.backend.DTOs.GroupCreationDTO;
import org.sportstogo.backend.Models.Group;
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

    @GetMapping(path="/chat-previews")
    public ResponseEntity<List<ChatPreviewDTO>> getChatPreviews(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        List<ChatPreviewDTO> groupPreviews = groupService.getChatPreviews(uid);

        return ResponseEntity.ok(groupPreviews);
    }

    @PostMapping(path="/create")
    public ResponseEntity<Group> createGroup(@RequestBody GroupCreationDTO groupCreationDTO, Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        Group createdGroup = groupService.createGroup(groupCreationDTO, uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }
}
