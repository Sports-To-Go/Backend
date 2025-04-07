package org.sportstogo.backend.Group;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.User.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GroupService {
    private GroupRepository groupRepository;
    public List<Group> get_groups() {
        return groupRepository.findAll();
    }
    public void add_group(Group group) {
        groupRepository.save(group);
    }
    @Transactional
    public void update_group(Long id, String name) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            throw new IllegalArgumentException("Group with id " + id + " does not exist");
        }
        group.get().setName(name);
    }

    public void delete_group(Long id) {
        if  (!groupRepository.existsById(id)) {
            throw new IllegalArgumentException("Group with id " + id + " does not exist");
        }
        groupRepository.deleteById(id);
    }
}
