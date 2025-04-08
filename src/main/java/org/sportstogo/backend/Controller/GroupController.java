package org.sportstogo.backend.Controller;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "groups")
@AllArgsConstructor
public class GroupController {
    private GroupService groupService;
    @GetMapping
    public List<Group> getGroups() {
        return groupService.getGroups();
    }
    @PostMapping
    public ResponseEntity<String> addGroups(@RequestBody Group group) {
        group.setCreatedDate(LocalDate.now());
        groupService.addGroup(group);
        return ResponseEntity.ok()
                .body("Group added successful");
    }
    @PutMapping(path = "{group_id}")
    public ResponseEntity<String> updateGroup(@PathVariable("group_id") Long id,
                             @RequestParam(required = true) String name) {
        groupService.updateGroup(id, name);
        return ResponseEntity.ok()
                .body("Group updated successful");
    }
    @Transactional
    @DeleteMapping(path = "{group_id}")
    public ResponseEntity<String> deleteGroup(@PathVariable("group_id") Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok()
                .body("Group deleted successful");
    }
}
