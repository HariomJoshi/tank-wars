package com.tankwars.frontend.client;

import com.tankwars.frontend.utils.Constants;
import com.tankwars.frontend.utils.User;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class StompClient {
    private static StompClient stompClient;

    private StompClient() {
    }

    ;

    public static StompClient getInstance() {
        if (stompClient == null) stompClient = new StompClient();
        return stompClient;
    }

    public static StompSession stompSession;

    public void connect(String username) throws ExecutionException, InterruptedException {
        // Define the WebSocket endpoint, same as configured in your Spring backend
        String url = Constants.BACKEND_URL_WS + "?username=" + User.getInstance().getUsername();


        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

//        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
//        headers.add("username", User.getInstance().getUsername());
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        CustomStompSessionHandler sessionHandler = new CustomStompSessionHandler(username);
        stompSession = stompClient.connectAsync(url, sessionHandler).get();
    }

    // Method to send messages
    public void sendMessage(String destination, Object payload) {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.send(destination, payload);
        } else {
            System.out.println("Not Connected, how can I send message?");
        }
    }

    // Method to disconnect
    public void disconnect() {
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
        } else {
            System.out.println("Not Connected, how can I disconnect?");
        }
    }
}
