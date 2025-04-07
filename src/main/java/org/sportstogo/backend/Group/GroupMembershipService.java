package org.sportstogo.backend.Group;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.sportstogo.backend.GroupMembership.GroupMembership;
import org.sportstogo.backend.GroupMembership.GroupMembershipId;
import org.sportstogo.backend.GroupMembership.GroupMembershipRepo;
import org.sportstogo.backend.GroupMembership.Role;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Getter @Setter
public class GroupMembershipService {
    private GroupMembershipRepo groupMembershipRepo;

    public List<GroupMembership> get_group_memberships() {
        return groupMembershipRepo.findAll();
    }

    public void add_group_membership(GroupMembership groupMembership) {
        groupMembership.setJoinTime(LocalDateTime.now());
        groupMembershipRepo.save(groupMembership);
    }

    public void update_group_membership(GroupMembershipId groupMembershipId, Role role) {
            Optional<GroupMembership> groupMembership = groupMembershipRepo.findById(groupMembershipId);
            if  (groupMembership.isEmpty()) {
                throw new IllegalArgumentException("GroupMembership not found");
            }
            groupMembership.get().setRole(role);
    }

    public void delete_group_membership(GroupMembershipId groupMembershipId) {
        groupMembershipRepo.deleteById(groupMembershipId);
    }
}
