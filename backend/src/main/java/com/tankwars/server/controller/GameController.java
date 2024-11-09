package com.tankwars.server.controller;


import com.tankwars.server.model.Message.InviteMessage;
import com.tankwars.server.model.Message.ResponseMessage;
import com.tankwars.server.utils.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import com.tankwars.server.model.GameState;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller  // marks the class as Spring MVC controller
// Therefore, it helps us define endpoints for websocket messages, not HTTP requests
public class GameController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRegistry userRegistry = UserRegistry.getInstance();

    @Autowired
    public GameController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/{roomId}/invite")
    @SendTo("/queue/invite")
    public InviteMessage sendInvite(@Payload InviteMessage inviteMessage, @DestinationVariable String roomId) {
        String inviteeUsername = inviteMessage.getInviteeUsername();
        System.out.println("IN SERVER: inviting user: "+ inviteeUsername);
//        // Send invite to specific user queue
//        messagingTemplate.convertAndSendToUser(inviteeUsername,"/queue/invite", inviteMessage, createHeaders(inviteeUsername));
//        // Start invitation timer
//        startInvitationTimer(inviteMessage.getInviterUsername(), inviteeUsername, roomId);
        return inviteMessage;
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    @Autowired
    private SimpUserRegistry simpUserRegistry;
    public void logActiveSessions() {
        System.out.println("Active users and their sessions:");
        for (SimpUser user : simpUserRegistry.getUsers()) {
            System.out.println("User: " + user.getName());
            for (SimpSession session : user.getSessions()) {
                System.out.println(" Session ID: " + session.getId());
                for (SimpSubscription subscription : session.getSubscriptions()) {
                    System.out.println("  Subscribed to: " + subscription.getDestination());
                }
            }
        }
    }



    @MessageMapping("/game/{roomId}/accept")
    public void acceptInvite(@Payload ResponseMessage response, @DestinationVariable String roomId) {
        if (response.isAccepted()) {
            // Notify both players that the game session is starting
            messagingTemplate.convertAndSend("/topic/game/" + roomId, "Game Started");
        } else {
            // Notify the inviter that the invite was declined
            messagingTemplate.convertAndSendToUser(response.getInviterUsername(), "/queue/invite", new ResponseMessage(false, response.getInviterUsername()));
        }
    }

    private void startInvitationTimer(String inviterUsername, String inviteeUsername, String roomId) {
        new Thread(() -> {
            try {
                Thread.sleep(30000); // Wait 30 seconds
                // Check if invitee accepted
                messagingTemplate.convertAndSendToUser(inviterUsername, "/queue/invite", new ResponseMessage(false, inviterUsername));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    // Invitation logic ends here, below are the routes to handle game events

    @MessageMapping("/game/{roomId}/fire")
    @SendTo("/topic/game/{roomId}")
    public GameState fireWeapon(GameState state, @DestinationVariable String roomId) {
        // Handle the fire action from a player
        System.out.println("Player fired in room: " + roomId);
        // Update the game state based on the action, e.g., apply damage
        return state;  // Broadcast updated state to players in the room
    }

    @MessageMapping("/game/{roomId}/join")
    @SendTo("/topic/game/{roomId}")
    public GameState joinGame(GameState state, @DestinationVariable String roomId) {
        // Handle player joining a game room
        System.out.println("Player joined room: " + roomId);
        return state;  // Broadcast current game state to both players
    }

    // register events
    @MessageMapping("/register")
    public void registerUser(@Payload String username, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();  // Get the session ID
        userRegistry.register(username, sessionId);
        System.out.println("User connected: " + username + " with session ID: " + sessionId);
    }

    // Handle user disconnection
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String username = userRegistry.getUsernameBySessionId(event.getSessionId());  // Implement this method to retrieve the username
        userRegistry.unregister(username);
        System.out.println("User disconnected: " + username);
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        logActiveSessions();
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        logActiveSessions();
    }
}
