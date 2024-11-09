package com.tankwars.frontend.client;

import com.tankwars.frontend.models.GameState;
import com.tankwars.frontend.models.InviteMessage;
import javafx.application.Platform;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    private final String username;

    public CustomStompSessionHandler(String username)  {
        this.username = username;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Connected to WebSocket server as: " + username);

        // Store the session to use later
        StompClient.stompSession = session;

        // Register the user by sending a message to the /app/register endpoint
        session.send("/app/register", username);
        System.out.println("Session id for " + username+ " is : " +session.getSessionId());

        // Subscribe to a game topic or user queue to receive messages
        session.subscribe("/topic/game/{roomId}", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return GameState.class;  // Adjust this as per your model
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try{
                    if(payload instanceof GameState){
                        GameState updatedState = (GameState) payload;
                        // you can use following to run in separate thread
//                executorService.submit(() -> updateUIWithGameState(updatedState));
                        Platform.runLater(() -> {
                            // this should be on a seperate Thread
                            // TODO: Commenting it for now since no such functions exists but we have to do the update action here only in a seperate thread
                            // updateGameUI(updatedState);
                        });  // Update the JavaFX UI
                    }else{
                        System.out.println("Recieved unexpected payload type: " + payload.getClass());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        session.subscribe("/queue/invite", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return InviteMessage.class;
            }

            public void handleFrame(StompHeaders headers, Object payload) {
                InviteMessage inviteMessage = (InviteMessage) payload;

                System.out.println("Invitation recieved!");
                Platform.runLater(() -> {
                    // TODO: logic to update dashboard
                    System.out.println("You are invited by the user: " + inviteMessage.getInviterUsername());
                });
            }
        });

        // Add more subscriptions here as needed
    }


    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println(username);
        System.out.println(session.getSessionId());
        System.out.println(session.isConnected());
        System.out.println("Connection error: " + exception.getMessage());
        exception.printStackTrace();
    }
}
