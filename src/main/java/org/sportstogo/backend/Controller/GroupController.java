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
@RequestMapping(path = "/groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public List<Group> getGroups() {
        return groupService.getGroups();
    }

    @PostMapping
    public ResponseEntity<?> addGroups(@RequestBody Group group) {
        group.setCreatedDate(LocalDate.now());
        return groupService.addGroup(group);
    }

    @PutMapping(path = "{group_id}")
    public ResponseEntity<?> updateGroup(@PathVariable("group_id") Long id,
                                         @RequestParam String name) {
        return groupService.updateGroup(id, name);
    }

    @Transactional
    @DeleteMapping(path = "{group_id}")
    public ResponseEntity<?> deleteGroup(@PathVariable("group_id") Long id) {
        return groupService.deleteGroup(id);
    }
}
