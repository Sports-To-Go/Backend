package org.sportstogo.backend.WebSocketHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.sportstogo.backend.Service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@AllArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
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
//            System.out.println("Authenticated user: " + uid);

            // Save user/session mapping
            session.getAttributes().put("uid", uid);

            // Add user session to the service
            chatService.addUserSession(uid, session);

//            System.out.println("User " + uid + " connected. Active sessions: " + chatService.getActiveSessionCount());

        } catch (Exception e) {
            System.out.println("Auth error: " + e.getMessage());
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Authentication failed"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received: " + message.getPayload());
        chatService.handleNewMessage(session, message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WebSocket error: " + exception.getMessage());
        String uid = (String) session.getAttributes().get("uid");
        if (uid != null) {
            chatService.removeUserSession(uid);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uid = (String) session.getAttributes().get("uid");
        if (uid != null) {
            chatService.removeUserSession(uid);
//            System.out.println("User " + uid + " disconnected. Reason: " + status.getReason() +
//                    ". Active sessions: " + chatService.getActiveSessionCount());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}