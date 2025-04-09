package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Repository.GroupMembershipRepo;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.MessageRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembershipRepo groupMembershipRepo;
    private final MessageRepository messageRepository;
    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    public ResponseEntity<?> addGroup(Group group) {
        groupRepository.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body("Group successfully added");
    }

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

    public ResponseEntity<?> deleteGroup(Long id) {
        if (!groupRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group with id " + id + " does not exist");
        }
        //delete all the members
        List<GroupMembership> groupMemberships = groupMembershipRepo.findByGroupId(id);
        groupMembershipRepo.deleteAll(groupMemberships);

        //delete all the messages inside the group
        List<Message>  messages = messageRepository.findByGroupId(id);
        messageRepository.deleteAll(messages);
        //delete the group
        groupRepository.deleteById(id);
        return ResponseEntity.ok("Group successfully deleted");
    }
}
