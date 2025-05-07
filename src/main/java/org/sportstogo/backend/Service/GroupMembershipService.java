package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Repository.GroupMembershipRepository;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.MessageRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupMembershipService {

    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;

    public boolean isMemberOfGroup(String uid, Long groupID) {
        return groupMembershipRepository.existsByUserIDAndGroupID(uid, groupID);
    }

    public boolean removeUserFromGroup(String uid, Long groupID) {
        GroupMembership membership = groupMembershipRepository.findByUserIDAndGroupID(uid, groupID);
        if (membership != null) {
            groupMembershipRepository.delete(membership);

            // Check if the group has any remaining members
            boolean hasMembers = groupMembershipRepository.existsByGroupID(groupID);
            if (!hasMembers) {
                messageRepository.deleteAllByGroupId(groupID);
                groupRepository.deleteById(groupID);
            }

            return true;
        }
        return false;
    }
}
