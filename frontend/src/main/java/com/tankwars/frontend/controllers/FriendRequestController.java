package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.Animations;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestController {
    @FXML
    private ListView<FriendRequest> listViewFriendRequests;
    @FXML
    private Button buttonBack;

    private List<FriendRequest> listFriendRequests = new ArrayList<>();
    private Scene previousScene;

    @FXML
    public void initialize() {
        // Set the cell factory to use your custom FriendRequestCell
        listViewFriendRequests.setCellFactory(lv -> new FriendRequestCell());

        // Dummy requests
        for (int i = 1; i <= 30; i++) {
            listFriendRequests.add(new FriendRequest("User " + i, Integer.toString(i * 10)));
        }
        updateRequests();

        buttonBack.setOnAction(e -> handleReturn((Node) e.getSource()));
        buttonBack.setOnMouseEntered(e -> Animations.mouseEnterTransition(buttonBack));
        buttonBack.setOnMouseExited(e -> Animations.mouseExitTransition(buttonBack));
    }

    private void updateRequests() {
        listViewFriendRequests.getItems().clear(); // Clear current items
        listViewFriendRequests.getItems().addAll(listFriendRequests); // Add updated list of invites
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    private void handleReturn(Node node) {
        Stage currentStage = (Stage) node.getScene().getWindow();
        if (previousScene != null && currentStage != null) {
            Animations.fadeScenes(currentStage, currentStage.getScene(), previousScene);
        }
    }

    class FriendRequest {
        private String username;
        private String requestId; // Unique ID for the friend request

        public FriendRequest(String username, String requestId) {
            this.username = username;
            this.requestId = requestId;
        }

        public String getUsername() {
            return username;
        }

        public String getRequestId() {
            return requestId;
        }

        @Override
        public String toString() {
            return username; // Display only the username in the ListView
        }
    }

    public class FriendRequestCell extends ListCell<FriendRequest> {
        @Override
        protected void updateItem(FriendRequest request, boolean empty) {
            super.updateItem(request, empty);

            if (empty || request == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Create the layout for the cell
                HBox hbox = new HBox(10); // 10px spacing
                hbox.setStyle("-fx-padding: 10;"); // Add padding to the HBox
                hbox.setPrefHeight(50); // Set preferred height for each cell

                // Create a Text node for the username
                Text usernameText = new Text(request.getUsername());
                usernameText.setWrappingWidth(150); // Set max width for the text
                usernameText.setTextAlignment(TextAlignment.LEFT);
                usernameText.setStyle("-fx-ellipsis-string: '...';"); // Add ellipsis for overflow

                // Create buttons with fixed width
                Button acceptButton = new Button("Accept");
                Button rejectButton = new Button("Reject");
                acceptButton.setPrefWidth(70); // Fixed width for the accept button
                rejectButton.setPrefWidth(70); // Fixed width for the reject button

                // Set actions for the buttons
                acceptButton.setOnAction(event -> {
                    System.out.println(request.getUsername() + " accepted.");
                    listFriendRequests.remove(request); // Remove request from the list
                    updateRequests(); // Update the ListView
                });

                rejectButton.setOnAction(event -> {
                    System.out.println(request.getUsername() + " rejected.");
                    listFriendRequests.remove(request); // Remove request from the list
                    updateRequests(); // Update the ListView
                });

                // Add components to the layout
                HBox.setHgrow(usernameText, Priority.ALWAYS); // Allow text to grow and fill space
                hbox.getChildren().addAll(usernameText, acceptButton, rejectButton);
                setGraphic(hbox);
            }
        }
    }
}
