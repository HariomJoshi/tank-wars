package com.tankwars.server.websockets;

import com.tankwars.server.model.stomp.StompPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // Assuming we have the username from the request or session
        URI uri = request.getURI();
        String username = UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("username");
        System.out.println("Handshake Request from: "+username);
        if (username != null) {
            attributes.put("user", new StompPrincipal(username));
        }else{
            System.out.println("Username is null in handshake interceptor.");
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Nothing to implement here
        System.out.println("Hanshake happened");
    }
}
