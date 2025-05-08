package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.JoinRequest;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.JoinRequestRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.sportstogo.backend.idModels.GroupMemberID;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JoinRequestService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final JoinRequestRepository joinRequestRepository;

    public JoinRequest addJoinRequest(String uid, Long groupId) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found with uid: " + uid));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

        GroupMemberID id = new GroupMemberID(uid, groupId);
        if (joinRequestRepository.existsById(id)) {
            throw new IllegalStateException("Join request already exists for this user and group.");
        }

        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserID(user);
        joinRequest.setGroupID(group);
        joinRequest.setMotivation("");  // empty string

        return joinRequestRepository.save(joinRequest);
    }


}
