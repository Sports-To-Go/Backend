package org.sportstogo.backend.Service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.GroupMembershipId;
import org.sportstogo.backend.Repository.GroupMembershipRepo;
import org.sportstogo.backend.Models.Role;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Getter @Setter
public class GroupMembershipService {
    private GroupMembershipRepo groupMembershipRepo;

    public List<GroupMembership> getGroupMemberships() {
        return groupMembershipRepo.findAll();
    }

    public void add_group_membership(GroupMembership groupMembership) {
        groupMembership.setJoinTime(LocalDateTime.now());
        groupMembershipRepo.save(groupMembership);
    }

    public void updateGroupMembership(GroupMembershipId groupMembershipId, Role role) {
            Optional<GroupMembership> groupMembership = groupMembershipRepo.findById(groupMembershipId);
            if  (groupMembership.isEmpty()) {
                throw new IllegalArgumentException("GroupMembership not found");
            }
            groupMembership.get().setRole(role);
    }

    public void deleteGroupMembership(GroupMembershipId groupMembershipId) {
        groupMembershipRepo.deleteById(groupMembershipId);
    }
}
