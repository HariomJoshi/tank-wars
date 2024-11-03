package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.Animations;
import com.tankwars.frontend.utils.ApiClient;
import com.tankwars.frontend.utils.Constants;
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

public class FriendRequestController {
    @FXML
    private ListView<FriendRequest> listViewFriendRequests;
    @FXML
    private Button buttonBack;

    private List<FriendRequest> listFriendRequests = new ArrayList<>();
    private Scene previousScene;
    private ApiClient client = new ApiClient();
    private User currentUser = User.getInstance();

    @FXML
    public void initialize() {
        // Set the cell factory to use your custom FriendRequestCell
        listViewFriendRequests.setCellFactory(lv -> new FriendRequestCell());
        String params = String.format("?username=%s", currentUser.getUsername());
        String fullUrl = Constants.BACKEND_URL + "api/user/getRequests" + params;
        client.sendGetReq(fullUrl).thenAccept(response -> {
            Platform.runLater(() -> {
                if (!response.isEmpty()) {
                    System.out.println(response);
                    for (int i = 0; i < response.size(); i++) {
                        listFriendRequests.add(new FriendRequest(response.get(i)));
                    }
                    updateRequests();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("No new Friend Requests");
                    alert.show();
                }
            });
        }).exceptionally(error -> {
            System.out.println("Some error occured");
            error.printStackTrace();
            return null;
        });


        buttonBack.setOnAction(e -> handleReturn((Node) e.getSource()));
        buttonBack.setOnMouseEntered(e -> Animations.mouseEnterTransition(buttonBack));
        buttonBack.setOnMouseExited(e -> Animations.mouseExitTransition(buttonBack));
    }

    // just to refresh the friends array from the newly updated list
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


    // single friend Request
    class FriendRequest {
        private String username;

        public FriendRequest(String username) {
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

    // creating a list of friend requests from the list of friends
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
                    String QueryParams = String.format("?currentUsername=%s&requesterUsername=%s", currentUser.getUsername(), request.getUsername());
                    String fullUrl = Constants.BACKEND_URL + "api/user/acceptRequest" + QueryParams;
                    client.sendPostReqQuery(fullUrl).thenAccept(response -> {
                        Platform.runLater(() -> {
                            if (Boolean.TRUE.equals(response)) {
                                System.out.println(request.getUsername() + " accepted.");
                                listFriendRequests.remove(request); // Remove request from the list
                                updateRequests(); // Update the ListView
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Not able to accept friend request");
                                alert.show();
                                System.out.println("Not able to accept friend request");
                            }
                        });

                    }).exceptionally(error -> {
                        System.out.println("Some error occured while accepting friend request");
                        error.printStackTrace();
                        return null;
                    });

                });

                rejectButton.setOnAction(event -> {
                    String QueryParams = String.format("?currentUsername=%s&requesterUsername=%s", currentUser.getUsername(), request.getUsername());
                    String fullUrl = Constants.BACKEND_URL + "api/user/rejectRequest" + QueryParams;
                    client.sendPostReqQuery(fullUrl).thenAccept(response -> {
                        Platform.runLater(() -> {
                            if (Boolean.TRUE.equals(response)) {
                                System.out.println(request.getUsername() + " rejected.");
                                listFriendRequests.remove(request); // Remove request from the list
                                updateRequests(); // Update the ListView
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Not able to reject friend request");
                                alert.show();
                                System.out.println("Not able to reject friend request");
                            }
                        });

                    }).exceptionally(error -> {
                        System.out.println("Some error occured while accepting friend request");
                        error.printStackTrace();
                        return null;
                    });
                });

                // Add components to the layout
                HBox.setHgrow(usernameText, Priority.ALWAYS); // Allow text to grow and fill space
                hbox.getChildren().addAll(usernameText, acceptButton, rejectButton);
                setGraphic(hbox);
            }
        }
    }
}
