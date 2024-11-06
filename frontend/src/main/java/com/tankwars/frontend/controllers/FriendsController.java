package com.tankwars.frontend.controllers;

import com.tankwars.frontend.client.StompClient;
import com.tankwars.frontend.models.InviteMessage;
import com.tankwars.frontend.tankwarsclient.Animations;
import com.tankwars.frontend.utils.ApiClient;
import com.tankwars.frontend.utils.Constants;
import com.tankwars.frontend.utils.RandomString;
import com.tankwars.frontend.utils.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FriendsController {
    @FXML
    private ListView<Friend> listViewFriends;
    @FXML
    private Button buttonBack;
    private User currentUser = User.getInstance();
    private ApiClient client = new ApiClient();
    private List<Friend> listFriends = new ArrayList<>();
    private Scene previousScene;

    @FXML
    public void initialize() {
        // Set the cell factory to use your custom FriendCell
        listViewFriends.setCellFactory(lv -> new FriendCell());
        String params = String.format("?username=%s", currentUser.getUsername());
        String fullUrl = Constants.BACKEND_URL + "api/user/myFriends" + params;
        client.sendGetReq(fullUrl).thenAccept(response -> {
            Platform.runLater(() -> {
                if (!response.isEmpty()) {
                    for (int i = 0; i < response.size(); i++) {
                        listFriends.add(new Friend(response.get(i)));
                    }
                    updateFriends();
                    System.out.println("Results fetched successfully");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("No friends found");
                    alert.show();
                    System.out.println("No friends found");
                }
            });

        }).exceptionally(err -> {
            System.out.println("Some error occured while fetching friends list");
            err.printStackTrace();
            return null;
        });

        buttonBack.setOnAction(e -> handleReturn((Node) e.getSource()));
    }

    private void updateFriends() {
        listViewFriends.getItems().clear(); // Clear current items
        listViewFriends.getItems().addAll(listFriends); // Add updated list of friends
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    private void handleReturn(Node node) {
        Stage currentStage = (Stage) node.getScene().getWindow();

        if (previousScene != null && currentStage != null) {
            currentStage.setScene(previousScene); // Set the previous scene
            Animations.fadeScenes(currentStage, currentStage.getScene(), previousScene);
        }
    }

    class Friend {
        private String username;
        private String userId; // Unique ID for the friend

        public Friend(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }


        @Override
        public String toString() {
            return username; // Display only the username in the ListView
        }
    }

    public class FriendCell extends ListCell<Friend> {
        @Override
        protected void updateItem(Friend friend, boolean empty) {
            super.updateItem(friend, empty);

            if (empty || friend == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Create the layout for the cell
                HBox hbox = new HBox(10); // 10px spacing
                hbox.setStyle("-fx-padding: 10;"); // Add padding to the HBox
                hbox.setPrefHeight(50); // Set preferred height for each cell

                // Create a Text node for the username
                Text usernameText = new Text(friend.getUsername());
                usernameText.setWrappingWidth(150); // Set max width for the text
                usernameText.setTextAlignment(TextAlignment.LEFT);
                usernameText.setStyle("-fx-ellipsis-string: '...';"); // Add ellipsis for overflow

                // Create a remove button
                Button removeButton = new Button("Remove");
                removeButton.setPrefWidth(70); // Fixed width for the remove button

                // create an invite button
                Button inviteButton = new Button("Invite");
                removeButton.setPrefWidth(70); // Fixed width for the remove button

                // Set action for the remove button
                removeButton.setOnAction(event -> {
                    System.out.println(friend.getUsername() + " removed.");
                    listFriends.remove(friend); // Remove friend from the list
                    updateFriends(); // Update the ListView
                });

                // Invite a user here
                StompClient client = StompClient.getInstance();
                inviteButton.setOnAction(event -> {
                    System.out.println(friend.getUsername() + " invited.");
                    String url = "/app/game/" + RandomString.generateRandomString() + "/invite";
                    System.out.println("Hariom sent to URL: " + url);
                    client.sendMessage(url, new InviteMessage(currentUser.getUsername(), friend.getUsername()));
                    System.out.println("Invitation sent from :" + currentUser.getUsername());
                });

                // Add components to the layout
                HBox.setHgrow(usernameText, Priority.ALWAYS); // Allow text to grow and fill space
                hbox.getChildren().addAll(usernameText, removeButton, inviteButton);
                setGraphic(hbox);
            }
        }
    }
}
