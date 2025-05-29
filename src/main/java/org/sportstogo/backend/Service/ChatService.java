package org.sportstogo.backend.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.MessageDTO;
import org.sportstogo.backend.Enums.MessageType;
import org.sportstogo.backend.Enums.Theme;
import org.sportstogo.backend.Models.GroupMembership;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Repository.GroupMembershipRepository;
import org.sportstogo.backend.Repository.GroupRepository;
import org.sportstogo.backend.Repository.MessageRepository;
import org.sportstogo.backend.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ChatService {
    // Map from userId to their WebSocket session
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMembershipRepository groupMembershipRepository;


    /**
     * Add a user session
     */
    public void addUserSession(String userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    /**
     * Remove a session from all groups
     */
    public void removeUserSession(String userId) {
        userSessions.remove(userId);
    }

    /**
     * Handle incoming message from a user
     */
    public void handleNewMessage(WebSocketSession senderSession, String messageContent) {
        try {
            var now = LocalDateTime.now();
            MessageDTO incomingMessage = objectMapper.readValue(messageContent, MessageDTO.class);
            String senderId = (String) senderSession.getAttributes().get("uid");
            Long groupId = incomingMessage.getGroupID(); // Get groupId from message payload

            if (groupId == null || senderId == null) {
                System.err.println("Missing groupId or senderId");
                return;
            }

            // Verify user is member of the group
            GroupMembership groupMembership = groupMembershipRepository.findByUserIDAndGroupID(senderId, groupId);
            if (groupMembership == null) {
                System.out.println("User " + senderId + " is not a member of group " + groupId);
                return;
            }

            // Create and save message
            Message message = new Message();
            message.setGroupID(groupMembership.getGroupID());
            message.setUserID(groupMembership.getUserID());
            message.setContent(incomingMessage.getContent());
            message.setTimeSent(LocalDateTime.now());
            message.setType(incomingMessage.getType());
            message.setSystemEvent(incomingMessage.getSystemEvent());
            message.setMetaData(objectMapper.writeValueAsString(incomingMessage.getMeta()));


            Long id = messageRepository.insert(groupId, senderId, message.getContent(), message.getTimeSent(),
                    message.getType().ordinal(), message.getSystemEvent(), message.getMetaData());
//            System.out.println("Message processing time: " +
//                    LocalDateTime.now().minusNanos(now.getNano()).getNano() + " nanoseconds");
            System.out.println(message.getSystemEvent());
            System.out.println(message.getType());
            if (message.getType() == MessageType.SYSTEM) {
                if (message.getSystemEvent().equals("THEME_CHANGED")) {
                    groupRepository.updateGroupTheme(
                            incomingMessage.getGroupID(),
                            Theme.valueOf((String) incomingMessage.getMeta().get("themeName"))
                    );
                }

                if (message.getSystemEvent().equals("NICKNAME_CHANGED")) {
                    Map<String, Object> meta = incomingMessage.getMeta();

                    String userId = (String) meta.get("uid");
                    String newNickname = (String) meta.get("newNickname");

                    Long groupId_ = incomingMessage.getGroupID();

                    groupMembershipRepository.updateNickname(userId, groupId_, newNickname);
                }
            }
            // Convert to DTO for broadcasting
            message.setID(id);
            MessageDTO messageDTO = message.toDTO();
            String jsonMessage = objectMapper.writeValueAsString(messageDTO);

            // Broadcast to all users in the group
            broadcastToGroupMembers(groupId, new TextMessage(jsonMessage));

        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Send a message to all users who are members of a specific group
     */
    private void broadcastToGroupMembers(Long groupId, TextMessage message) {
        try {
            // Get all members of the group
            List<GroupMembership> groupMembers = groupMembershipRepository.findByGroupID(groupId);

            for (GroupMembership membership : groupMembers) {
                String userId = membership.getUserID().getUid();
                WebSocketSession session = userSessions.get(userId);

                if (session != null && session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        System.err.println("Failed to send message to user " + userId + ": " + e.getMessage());
                        // Remove stale session
                        userSessions.remove(userId);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error broadcasting to group members: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createSystemMessage(Long groupId, String senderUid, String eventType, Map<String, Object> meta) {
        try {
            Message message = new Message();
            message.setGroupID(groupRepository.getReferenceById(groupId));
            message.setUserID(userRepository.getReferenceById(senderUid));
            message.setType(MessageType.SYSTEM);
            message.setSystemEvent(eventType);
            message.setMetaData(new ObjectMapper().writeValueAsString(meta));
            message.setContent(""); // unused for system messages
            message.setTimeSent(LocalDateTime.now());

            message = messageRepository.save(message);

            // convert to DTO
            MessageDTO dto = message.toDTO();
            String json = new ObjectMapper().writeValueAsString(dto);
            broadcastToGroupMembers(groupId, new TextMessage(json));
        } catch (Exception e) {
            System.err.println("Failed to create system message: " + e.getMessage());
        }
    }

    /**
     * Get count of active sessions
     */
    public int getActiveSessionCount() {
        return userSessions.size();
    }
}