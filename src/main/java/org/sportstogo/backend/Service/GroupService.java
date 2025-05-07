package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.ChatPreviewDTO;
import org.sportstogo.backend.DTOs.GroupCreationDTO;
import org.sportstogo.backend.DTOs.GroupDataDTO;
import org.sportstogo.backend.DTOs.GroupMemberDTO;
import org.sportstogo.backend.Enums.Role;
import org.sportstogo.backend.Exceptions.UserNotFoundException;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Repository.GroupMembershipRepository;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.MessageRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final UserRepository userRepository;

    public List<ChatPreviewDTO> getChatPreviews(String uid) {
        return groupRepository.findChatPreviewsByUserId(uid);
    }

    public Group createGroup(GroupCreationDTO groupCreationDTO, String uid) {
        User creator = userRepository.findById(uid).orElse(null);
        if(creator == null) {
            throw new UserNotFoundException(uid);
        }

        Group group = new Group();
        group.setName(groupCreationDTO.getName());
        group.setCreatedBy(creator);
        Group createdGroup = groupRepository.save(group);

        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setGroupID(createdGroup);
        groupMembership.setUserID(creator);
        groupMembership.setRole(Role.admin);

        groupMembershipRepository.save(groupMembership);

        return createdGroup;
    }

    public GroupDataDTO getGroupData(Long groupID) {
        GroupDataDTO groupDataDTO = new GroupDataDTO();
        Group group = groupRepository.findById(groupID).orElse(null);
        if (group == null) {
            return null;
        }

        groupDataDTO.setDescription(group.getDescription());
        groupDataDTO.setName(group.getName());

        // Fetch group members
        List<GroupMembership> memberships = groupMembershipRepository.findByGroupID(group);
        List<GroupMemberDTO> groupMembers = memberships.stream()
                .map(membership -> new GroupMemberDTO(
                        FirebaseTokenService.getDisplayNameFromUid(membership.getUserID().getUid()),
                        membership.getUserID().getUid()
                ))
                .toList();
        groupDataDTO.setGroupMembers(groupMembers);

        return groupDataDTO;
    }

}
