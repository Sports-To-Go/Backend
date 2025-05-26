package org.sportstogo.backend.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.MessageDTO;
import org.sportstogo.backend.Enums.MessageType;
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
     * Remove a user session
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
            message.setType(MessageType.TEXT);

            // Save message
            Long id = messageRepository.insert(groupId, senderId, message.getContent(), message.getTimeSent());
            System.out.println("Message processing time: " +
                    LocalDateTime.now().minusNanos(now.getNano()).getNano() + " nanoseconds");

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

    /**
     * Check if user is member of group
     */
    public boolean isUserMemberOfGroup(String uid, Long groupId) {
        return groupMembershipRepository.existsByUserIDAndGroupID(uid, groupId);
    }

    /**
     * Get user session for testing/debugging purposes
     */
    public WebSocketSession getUserSession(String userId) {
        return userSessions.get(userId);
    }

    /**
     * Get count of active sessions
     */
    public int getActiveSessionCount() {
        return userSessions.size();
    }
}