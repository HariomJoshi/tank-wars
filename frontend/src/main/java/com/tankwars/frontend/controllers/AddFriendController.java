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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.nio.file.attribute.UserPrincipal;

public class AddFriendController {
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonOkayIfNotExists;
    @FXML
    private TextField boxUsername;
    @FXML
    private Button buttonSearchUser;
    @FXML
    private Label labelUserExsistsOrNot;
    @FXML
    private Button buttonAddUserIfExists;

    private Scene previousScene;

    // Setter for the previous scene
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }


    @FXML
    public void initialize() {
        buttonSearchUser.setOnMouseEntered(e -> Animations.mouseEnterTransition(buttonSearchUser));
        buttonSearchUser.setOnMouseExited(e -> Animations.mouseExitTransition(buttonSearchUser));
        buttonSearchUser.setOnAction(e -> {
            boxUsername.setEditable(false);
            boolean exists = handleSearchUser(boxUsername.getText());
            labelUserExsistsOrNot.setVisible(true);
            if (exists) {
                labelUserExsistsOrNot.setText("User Exists !");
                buttonAddUserIfExists.setVisible(true);
            } else {
                labelUserExsistsOrNot.setText("User doesn't exists! Please check the username");
                buttonOkayIfNotExists.setVisible(true);
            }
        });

        buttonAddUserIfExists.setOnAction(e -> handleAddFriend(boxUsername.getText()));
        buttonAddUserIfExists.setOnMouseEntered(e -> Animations.mouseEnterTransition(buttonAddUserIfExists));
        buttonAddUserIfExists.setOnMouseExited(e -> Animations.mouseExitTransition(buttonAddUserIfExists));

        buttonOkayIfNotExists.setOnAction(e -> handleOkay());
        buttonOkayIfNotExists.setOnMouseEntered(e -> Animations.mouseEnterTransition(buttonOkayIfNotExists));
        buttonOkayIfNotExists.setOnMouseExited(e -> Animations.mouseExitTransition(buttonOkayIfNotExists));

        buttonBack.setOnAction(e -> handleReturn((Node) e.getSource()));
        buttonBack.setOnMouseEntered(e -> Animations.mouseEnterTransition(buttonBack));
        buttonBack.setOnMouseExited(e -> Animations.mouseExitTransition(buttonBack));
    }

    private void handleReturn(Node node) {
        // Get the currently active stage (topmost)
        Stage currentStage = (Stage) node.getScene().getWindow();

        if (previousScene != null && currentStage != null) {
//            currentStage.setScene(previousScene); // Set the previous scene
            Animations.fadeScenes(currentStage, currentStage.getScene(), previousScene);
        }
    }

    private void handleOkay() {
        labelUserExsistsOrNot.setVisible(false);
        buttonOkayIfNotExists.setVisible(false);
        boxUsername.setEditable(true);
    }

    ApiClient client = new ApiClient();
    private User currentUser = User.getInstance();

    private void handleAddFriend(String targetUsername) {
        // code here to add a user as a friend iff exists
        String queryParams = String.format("?currentUsername=%s&targetUsername=%s", currentUser.getUsername(), targetUsername);
        String fullUrl = Constants.BACKEND_URL + "api/user/sendRequest" + queryParams;
        client.sendPostReqQuery(fullUrl).thenAccept(response -> {
            Platform.runLater(() -> {
                if (Boolean.TRUE.equals(response)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Friend request sent successfully");
                    alert.show();
                    System.out.println("Friend request sent");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Friend does not exists");
                    alert.show();
                    System.out.println("Friend does not exists");
                }
            });
        }).exceptionally(error -> {
            System.out.println("Some error occured while sending request");
            error.printStackTrace();
            return null;
        });
    }

    private boolean handleSearchUser(String username) {
        String queryParams = String.format("?username=%s", username);
        String fullUrl = Constants.BACKEND_URL + "api/user/userExists" + queryParams;
        try {
            boolean found = client.sendPostReqQuery(fullUrl).get();
            if (found) {
                System.out.println("User exists");
            } else {
                System.out.println("User doesn't exists");
            }
            return found;
        } catch (Exception err) {
            System.out.println("Some error occured while searching user");
            err.printStackTrace();
            return false;
        }
    }

}
