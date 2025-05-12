package org.sportstogo.backend.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.MessageDTO;
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
    private final Map<Long, Set<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    /**
     * Extract Group ID from WebSocket Session URI
     */
    private Long extractGroupId(WebSocketSession session) {
        Object groupIdStr = session.getAttributes().get("groupID");

        try {
            return Long.parseLong(groupIdStr.toString());
        } catch (NumberFormatException e) {
            System.err.println("Invalid group ID: " + groupIdStr);
            return null;
        }
    }

    /**
     * Add a session to a specific group
     */
    public void addSessionToGroup(WebSocketSession session) {
        Long groupId = extractGroupId(session);
        if (groupId == null) {
            return;
        }
        groupSessions.computeIfAbsent(groupId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    /**
     * Remove a session from all groups
     */
    public void removeSession(WebSocketSession session) {
        Long groupId = extractGroupId(session);
        if (groupId != null && groupSessions.containsKey(groupId)) {
            groupSessions.get(groupId).remove(session);
            if (groupSessions.get(groupId).isEmpty()) {
                groupSessions.remove(groupId);
            }
        }
    }

    /**
     * Get recent messages for a group
     */
    public List<Message> getRecentMessages(WebSocketSession session, LocalDateTime timestamp) {
        Long groupId = extractGroupId(session);
        if (groupId == null) {
            return List.of();
        }
        return messageRepository.findRecentByGroupId(groupId, timestamp);
    }

    /**
     * Save and broadcast a new message to all users in the group
     */
    public void handleNewMessage(WebSocketSession senderSession, String messageContent) {
        try {
            var now = LocalDateTime.now();
            MessageDTO incomingMessage = objectMapper.readValue(messageContent, MessageDTO.class);
            Long groupId = extractGroupId(senderSession);
            String senderId = (String) senderSession.getAttributes().get("uid");

            if (groupId == null || senderId == null) {
                return;
            }

            GroupMembership groupMembership = groupMembershipRepository.findByUserIDAndGroupID(senderId, groupId);



            if  (groupMembership == null) {
                System.out.println("GroupMembership not found");
                return;
            }

            // Create and save message
            Message message = new Message();
            message.setGroupID(groupMembership.getGroupID());
            message.setUserID(groupMembership.getUserID());
            message.setContent(incomingMessage.getContent());
            message.setTimeSent(LocalDateTime.now());

            // Save message
            Long id = messageRepository.insert(groupId, senderId, message.getContent(),message.getTimeSent());
            System.out.println(LocalDateTime.now().minusNanos(now.getNano()).getNano());
            // Convert to DTO for broadcasting
            message.setID(id);
            MessageDTO messageDTO = message.toDTO();
            String jsonMessage = objectMapper.writeValueAsString(messageDTO);

            // Broadcast to all users in the group
            broadcastToGroup(groupId, new TextMessage(jsonMessage));

        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
        }
    }

    /**
     * Send a message to all connected users in a group
     */
    private void broadcastToGroup(Long groupId, TextMessage message) {
        if (!groupSessions.containsKey(groupId)) {
            return;
        }

        for (WebSocketSession session : groupSessions.get(groupId)) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    System.err.println("Failed to send message to session: " + e.getMessage());
                }
            }
        }
    }

    public boolean isUserMemberOfGroup(String uid, Long groupId) {
        return groupMembershipRepository.existsByUserIDAndGroupID(uid, groupId);
    }
}
