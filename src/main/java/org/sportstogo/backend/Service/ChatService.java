package org.sportstogo.backend.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.DTOs.MessageDTO;
import org.sportstogo.backend.Models.Group;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.Models.User;
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
    public List<Message> getRecentMessages(WebSocketSession session, int limit) {
        Long groupId = extractGroupId(session);
        if (groupId == null) {
            return List.of();
        }
        return messageRepository.findRecentByGroupId(groupId, limit);
    }

    /**
     * Save and broadcast a new message to all users in the group
     */
    public void handleNewMessage(WebSocketSession senderSession, String messageContent) {
        try {
            MessageDTO incomingMessage = objectMapper.readValue(messageContent, MessageDTO.class);
            Long groupId = extractGroupId(senderSession);
            String senderId = (String) senderSession.getAttributes().get("uid");

            if (groupId == null || senderId == null) {
                return;
            }

            // Verify user belongs to the group
            if (!groupMembershipRepository.existsByUserIDAndGroupID(senderId, groupId)) {
                System.err.println("User " + senderId + " is not a member of group " + groupId);
                return;
            }

            // Find the group and user
            Optional<Group> groupOpt = groupRepository.findById(groupId);
            Optional<User> userOpt = userRepository.findById(senderId);

            if (groupOpt.isEmpty() || userOpt.isEmpty()) {
                System.err.println("Group or user not found");
                return;
            }

            Group group = groupOpt.get();
            User user = userOpt.get();

            // Create and save message
            Message message = new Message();
            Long maxId = messageRepository.findMaxIdByGroupId(groupId);
            Long nextId = (maxId != null) ? maxId + 1 : 1L;

            message.setID(nextId);
            message.setGroupID(group);
            message.setUserID(user);
            message.setContent(incomingMessage.getContent());
            message.setTimeSent(LocalDateTime.now());

            // Save message
            Message savedMessage = messageRepository.save(message);

            // Convert to DTO for broadcasting
            MessageDTO messageDTO = savedMessage.toDTO();
            String jsonMessage = objectMapper.writeValueAsString(messageDTO);

            // Broadcast to all users in the group
            broadcastToGroup(groupId, new TextMessage(jsonMessage));

        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
            e.printStackTrace();
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
