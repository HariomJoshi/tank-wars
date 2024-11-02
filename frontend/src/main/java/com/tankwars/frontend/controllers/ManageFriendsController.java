package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.Animations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageFriendsController {
    @FXML
    private Button buttonYourFriends;
    @FXML
    private Button buttonFriendRequests;
    @FXML
    private Button buttonAddFriend;
    @FXML
    private Button buttonBack;

    private final List<Button> buttonsManageFriends = new ArrayList<>();

    @FXML
    public void initialize(){
        buttonsManageFriends.add(buttonAddFriend);
        buttonsManageFriends.add(buttonYourFriends);
        buttonsManageFriends.add(buttonFriendRequests);
        buttonsManageFriends.add(buttonBack);

        setButtonProperties();

        buttonBack.setOnAction(e->handleReturn((Node) e.getSource()));

        buttonAddFriend.setOnAction(e-> {
            try {
                showAddFriend((Node)e.getSource());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonFriendRequests.setOnAction(e-> {
            try {
                showFriendRequests((Node) e.getSource());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonYourFriends.setOnAction(e-> {
            try {
                showFriends((Node)e.getSource());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void showFriends(Node source) throws IOException {
        Stage parent = (Stage) source.getScene().getWindow();
        FXMLLoader friendsLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/your-friends.fxml"));
        Scene friendsScene = new Scene(friendsLoader.load());
        FriendsController friendsController = friendsLoader.getController();
        friendsController.setPreviousScene(source.getScene());
        Animations.fadeScenes(parent, parent.getScene(), friendsScene);
    }


    private void showFriendRequests(Node source) throws IOException {
        Stage parent = (Stage) source.getScene().getWindow();
        FXMLLoader friendRequestsLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/friend-requests.fxml"));
        Scene friendRequestsScene = new Scene(friendRequestsLoader.load());
        FriendRequestController friendRequestController = friendRequestsLoader.getController();
        friendRequestController.setPreviousScene(source.getScene());
        Animations.fadeScenes(parent, parent.getScene(), friendRequestsScene);
    }

    private void setButtonProperties() {
        for(Button button : buttonsManageFriends){
            setGraphics(button);
            button.setOnMouseEntered(e->Animations.mouseEnterTransition(button));
            button.setOnMouseExited(e->Animations.mouseExitTransition(button));
        }
    }

    private void showAddFriend(Node source) throws IOException {
        Stage parent = (Stage) source.getScene().getWindow();
        FXMLLoader addFriendLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/add-friend.fxml"));
        Scene addFriendScene = new Scene(addFriendLoader.load());
        AddFriendController addFriendController = addFriendLoader.getController();

        // Pass the current scene as the previous scene
        addFriendController.setPreviousScene(source.getScene());
        Animations.fadeScenes(parent, parent.getScene(), addFriendScene);
//        parent.setScene(addFriendScene);
    }

    private void handleReturn(Node node) {
        // Get the currently active stage (topmost)
        Stage currentStage = (Stage) node.getScene().getWindow(); // Replace someNode with a reference to a node in the current scene

        if (currentStage != null) {
            currentStage.close(); // Close the current stage
        }
    }

    private void setGraphics(Button button) {
        String imagePath = "";
        if(button == buttonBack){
            imagePath = "/images/icon_return.png";
        } else if(button == buttonAddFriend){
            imagePath = "/images/icon_addfriend.png";
        } else if(button == buttonYourFriends){
            imagePath = "/images/icon_friends.png";
        } else {
            imagePath = "/images/icon_friend_request.png";
        }

        Image icon = new Image(getClass().getResourceAsStream(imagePath));
        ImageView graphic = new ImageView(icon);
        graphic.setFitWidth(25);
        graphic.setFitHeight(25);
        button.setGraphic(graphic);
    }

}
