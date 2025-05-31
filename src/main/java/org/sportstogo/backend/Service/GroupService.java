package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.*;
import org.sportstogo.backend.Enums.GroupRole;
import org.sportstogo.backend.Enums.Theme;
import org.sportstogo.backend.Exceptions.UserNotFoundException;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.JoinRequest;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.GroupMembershipRepository;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.JoinRequestRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.sportstogo.backend.Enums.Theme;
import java.util.List;
@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final UserRepository userRepository;
    private final JoinRequestRepository joinRequestRepository;

    public Group createGroup(GroupCreationDTO groupCreationDTO, String uid) {
        User creator = userRepository.findById(uid).orElse(null);
        if(creator == null) {
            throw new UserNotFoundException(uid);
        }

        Group group = new Group();
        group.setName(groupCreationDTO.getName());
        group.setDescription(groupCreationDTO.getDescription());
        group.setCreatedBy(creator);
        Group createdGroup = groupRepository.save(group);

        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setGroupID(createdGroup);
        groupMembership.setUserID(creator);
        groupMembership.setGroupRole(GroupRole.admin);

        groupMembershipRepository.save(groupMembership);

        return createdGroup;
    }

    public List<GroupDataDTO> getGroupData(String uid) {
        List<GroupDataDTO> groupData = groupMembershipRepository.findAllByUserID(uid);
        for(GroupDataDTO groupDataDTO : groupData) {
            Long id = groupDataDTO.getId();

            List<GroupMembership> memberships = groupMembershipRepository.findByGroupID(id);
            List<GroupMemberDTO> groupMembers = memberships.stream()
                .map(membership -> new GroupMemberDTO(
                        FirebaseTokenService.getDisplayNameFromUid(membership.getUserID().getUid()),
                        membership.getUserID().getUid(),
                        membership.getGroupRole(),
                        membership.getNickname()
                ))
                .toList();
            groupDataDTO.setGroupMembers(groupMembers);

            List<JoinRequest> joinRequests = joinRequestRepository.findByGroupID(id);
            List<JoinRequestDTO> joinRequestDTOs = joinRequests.stream().map(JoinRequest::toDTO).toList();
            groupDataDTO.setJoinRequests(joinRequestDTOs);
        }
        return groupData;
    }

    public List<GroupPreviewDTO> getGroupRecommendations(String uid) {
        return groupRepository.findGroupRecommendations(uid);
    }
}
