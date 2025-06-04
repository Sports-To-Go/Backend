package org.sportstogo.backend.Service;

import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.*;
import org.sportstogo.backend.Enums.GroupRole;
import org.sportstogo.backend.Enums.MessageType;
import org.sportstogo.backend.Exceptions.UserNotFoundException;
import org.sportstogo.backend.Models.*;
import org.sportstogo.backend.Repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final UserRepository userRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final MessageRepository messageRepository;
    private final ImageService imageService;

    public Group createGroup(String name, String description, String uid, Image image) {
        User creator = userRepository.findById(uid).orElse(null);
        if(creator == null) {
            throw new UserNotFoundException(uid);
        }

        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setCreatedBy(creator);
        group.setImage(image);
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

            List<GroupMemberDTO> groupMembers = groupMembershipRepository.findByGroupID(id).stream().map(GroupMembership::toDTO).toList();
            groupDataDTO.setGroupMembers(groupMembers);

            List<JoinRequest> joinRequests = joinRequestRepository.findByGroupID(id);
            List<JoinRequestDTO> joinRequestDTOs = joinRequests.stream().map(JoinRequest::toDTO).toList();
            groupDataDTO.setJoinRequests(joinRequestDTOs);
        }
        return groupData;
    }

    public GroupDataDTO getGroupDataById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));

        List<GroupMemberDTO> groupMembers = groupMembershipRepository.findByGroupID(groupId).stream().map(GroupMembership::toDTO).toList();

        List<JoinRequest> joinRequests = joinRequestRepository.findByGroupID(groupId);
        List<JoinRequestDTO> joinRequestDTOs = joinRequests.stream().map(JoinRequest::toDTO).toList();

        return new GroupDataDTO(group, groupMembers, joinRequestDTOs);
    }

    public List<GroupPreviewDTO> getGroupRecommendations(String uid) {
        return groupRepository.findGroupRecommendations(uid);
    }

    public List<GroupPreviewDTO> getGroupsWhereUserIsNotBasic(String uid) {
        return groupMembershipRepository.findGroupsWhereUserHasElevatedRole(uid);
    }

    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Step 1: Delete TEXT and SYSTEM messages
        messageRepository.deleteByGroupIdAndTypeIn(groupId, List.of(MessageType.TEXT, MessageType.SYSTEM));

        // Step 2: Get all IMAGE messages for this group
        List<Message> imageMessages = messageRepository.findByGroupIdAndType(groupId, MessageType.IMAGE);

        // Step 3: Delete associated images from S3
        for (Message message : imageMessages) {
            String url = message.getContent();
            imageService.deleteImageByUrl(url);
        }

        // Step 4: Delete IMAGE messages
        messageRepository.deleteByGroupIdAndType(groupId, MessageType.IMAGE);

        // Step 5: Delete join requests
        joinRequestRepository.deleteAllByGroupId(groupId);

        // Step 6: Delete group image (if hosted in S3)
        if (group.getImage() != null) {
            Image image = group.getImage();
            group.setImage(null);
            groupRepository.save(group);
            imageService.deleteImageEntity(image);
        }

        groupRepository.delete(group);
    }

}
