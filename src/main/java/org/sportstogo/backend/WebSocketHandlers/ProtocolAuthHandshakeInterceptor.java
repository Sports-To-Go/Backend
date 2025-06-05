package org.sportstogo.backend.WebSocketHandlers;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

public class ProtocolAuthHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String origin = request.getHeaders().getOrigin();
//        System.out.printf("Handshake Origin: {}\n", origin);

        if (!"https://sports-to-go.netlify.app".equals(origin)) {
//            System.out.printf("Origin {} not allowed\n", origin);
            return false; // Reject connection
        }

        List<String> protocols = request.getHeaders().get("Sec-WebSocket-Protocol");
        if (protocols != null && !protocols.isEmpty()) {
            String protocol = protocols.get(0);
//            System.out.printf("Setting Sec-WebSocket-Protocol: {}\n", protocol);
            response.getHeaders().add("Sec-WebSocket-Protocol", protocol);
            attributes.put("protocol", protocol);
        }

        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) { //no op
    }
}
