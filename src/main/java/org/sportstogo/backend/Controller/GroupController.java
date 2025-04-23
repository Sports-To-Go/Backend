package org.sportstogo.backend.Controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing Group entities.
 * Provides endpoints for CRUD operations on groups.
 */
@RestController
@RequestMapping(path = "/groups")
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * Retrieves all existing groups.
     *
     * @return a list of all groups
     */
    @GetMapping
    public List<Group> getGroups() {
        return groupService.getGroups();
    }

    /**
     * Creates a new group with the current date as creation date.
     *
     * @param group the group object from the request body
     * @return HTTP 201 if created, or appropriate error response
     */
    @PostMapping
    public ResponseEntity<?> addGroups(@RequestBody Group group) {
        group.setCreatedDate(LocalDate.now());
        return groupService.addGroup(group);
    }

    /**
     * Updates the name of an existing group.
     *
     * @param id   the ID of the group to update
     * @param name the new name of the group
     * @return HTTP 200 if updated, or 404 if not found
     */
    @PutMapping(path = "{group_id}")
    public ResponseEntity<?> updateGroup(@PathVariable("group_id") Long id,
                                         @RequestParam String name) {
        return groupService.updateGroup(id, name);
    }

    /**
     * Deletes a group by its ID.
     *
     * @param id the ID of the group to delete
     * @return HTTP 204 if deleted, or 404 if not found
     */
    @Transactional
    @DeleteMapping(path = "{group_id}")
    public ResponseEntity<?> deleteGroup(@PathVariable("group_id") Long id) {
        return groupService.deleteGroup(id);
    }
}
