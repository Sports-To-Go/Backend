package org.sportstogo.backend.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Repository.GroupMembershipRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupService {
    private GroupRepository groupRepository;
    private GroupMembershipRepo groupMembershipRepo;
    public List<Group> getGroups() {
        return groupRepository.findAll();
    }
    public void addGroup(Group group) {
        groupRepository.save(group);
    }
    @Transactional
    public void updateGroup(Long id, String name) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            throw new IllegalArgumentException("Group with id " + id + " does not exist");
        }
        group.get().setName(name);
    }

    public void deleteGroup(Long id) {
        if  (!groupRepository.existsById(id)) {
            throw new IllegalArgumentException("Group with id " + id + " does not exist");
        }
        List<GroupMembership> groupMemberships = groupMembershipRepo.findByGroupId(id);
        groupMembershipRepo.deleteAll(groupMemberships);
        groupRepository.deleteById(id);
    }
}
