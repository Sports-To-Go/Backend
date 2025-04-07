package org.sportstogo.backend.Group;


import lombok.AllArgsConstructor;
import org.sportstogo.backend.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/group")
@AllArgsConstructor
public class GroupController {
    private GroupService groupService;
    @GetMapping
    public List<Group> get_groups() {
        return groupService.get_groups();
    }
    @PostMapping
    public void add_group(@RequestBody Group group) {
        group.setCreatedDate(LocalDate.now());
        groupService.add_group(group);
    }
    @PutMapping(path = "{group_id}")
    public void update_group(@PathVariable("group_id") Long id,
                             @RequestParam(required = true) String name) {
        groupService.update_group(id, name);

    }
    @DeleteMapping(path = "{group_id}")
    public void delete_group(@PathVariable("group_id") Long id) {
        groupService.delete_group(id);
    }
}
