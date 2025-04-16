package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Repository.GroupMembershipRepo;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Group entities and related operations,
 * such as membership and messages within a group.
 */
@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembershipRepo groupMembershipRepo;
    private final MessageRepository messageRepository;

    /**
     * Retrieves all groups from the database.
     *
     * @return a list of all groups
     */
    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    /**
     * Adds a new group to the database.
     *
     * @param group the group entity to be saved
     * @return HTTP 201 if successful
     */
    public ResponseEntity<?> addGroup(Group group) {
        groupRepository.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body("Group successfully added");
    }

    /**
     * Updates the name of an existing group by ID.
     *
     * @param id   the ID of the group to update
     * @param name the new name for the group
     * @return HTTP 200 if updated, or 404 if group not found
     */
    @Transactional
    public ResponseEntity<?> updateGroup(Long id, String name) {
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group with id " + id + " does not exist");
        }
        Group group = groupOpt.get();
        group.setName(name);
        return ResponseEntity.ok("Group successfully updated");
    }

    /**
     * Deletes a group by ID, along with all its members and messages.
     *
     * @param id the ID of the group to delete
     * @return HTTP 200 if successful, or 404 if not found
     */
    public ResponseEntity<?> deleteGroup(Long id) {
        if (!groupRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group with id " + id + " does not exist");
        }

        // Delete all group memberships
        List<GroupMembership> groupMemberships = groupMembershipRepo.findByGroupId(id);
        groupMembershipRepo.deleteAll(groupMemberships);

        // Delete all messages inside the group
        List<Message> messages = messageRepository.findByGroupId(id);
        messageRepository.deleteAll(messages);

        // Delete the group itself
        groupRepository.deleteById(id);

        return ResponseEntity.ok("Group successfully deleted");
    }
}
