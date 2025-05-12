package org.sportstogo.backend.WebSocketHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Models.Message;
import org.sportstogo.backend.DTOs.MessageDTO;
import org.sportstogo.backend.Service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private static final int INITIAL_MESSAGE_LOAD = 20;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Extract groupID from the URI
        String path = Objects.requireNonNull(session.getUri()).getPath();
        String groupIdStr = path.substring(path.lastIndexOf("/") + 1);
        Long groupId;

        try {
            groupId = Long.parseLong(groupIdStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid group ID: " + groupIdStr);
            session.close(CloseStatus.BAD_DATA.withReason("Invalid group ID"));
            return;
        }

        System.out.println("New connection for groupID: " + groupId);

        // Extract JWT from the 'Sec-WebSocket-Protocol'
        String protocolHeader = session.getHandshakeHeaders().getFirst("Sec-WebSocket-Protocol");

        if (protocolHeader == null || protocolHeader.isEmpty()) {
            System.out.println("No JWT token provided in Sec-WebSocket-Protocol");
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing auth token"));
            return;
        }

        try {
            // Verify the Firebase JWT
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(protocolHeader);
            String uid = decodedToken.getUid();
            System.out.println("Authenticated user: " + uid);

            // Check if user is member of the group
            if (!chatService.isUserMemberOfGroup(uid, groupId)) {
                System.out.println("User " + uid + " not authorized for group " + groupId);
                session.close(CloseStatus.POLICY_VIOLATION.withReason("Not a member of this group"));
                return;
            }

            // Save user/session mapping
            session.getAttributes().put("uid", uid);
            session.getAttributes().put("groupID", groupId);

            // Add session to the group's session collection
            chatService.addSessionToGroup(session);

            // Send recent messages history to the newly connected user
            List<Message> recentMessages = chatService.getRecentMessages(session, INITIAL_MESSAGE_LOAD);

            // Convert each message to DTO and send
            ObjectMapper objectMapper = new ObjectMapper();
            for (Message msg : recentMessages) {
                MessageDTO messageDTO = msg.toDTO();
                String messageJson = objectMapper.writeValueAsString(messageDTO);
                session.sendMessage(new TextMessage(messageJson));
            }

        } catch (Exception e) {
            System.out.println("Auth error: " + e.getMessage());
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Authentication failed"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        chatService.handleNewMessage(session, message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WebSocket error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove the session from our tracking
        chatService.removeSession(session);
        System.out.println("WebSocket closed: " + status.getReason());
    }
}