package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.Animations;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    public void initialize(){
        buttonSearchUser.setOnMouseEntered(e->Animations.mouseEnterTransition(buttonSearchUser));
        buttonSearchUser.setOnMouseExited(e->Animations.mouseExitTransition(buttonSearchUser));
        buttonSearchUser.setOnAction(e->{
            boxUsername.setEditable(false);
            boolean exists = handleSearchUser(boxUsername.getText());
            labelUserExsistsOrNot.setVisible(true);
            if(exists){
                labelUserExsistsOrNot.setText("User Exists !");
                buttonAddUserIfExists.setVisible(true);
            }else{
                labelUserExsistsOrNot.setText("User doesn't exists! Please check the username");
                buttonOkayIfNotExists.setVisible(true);
            }
        });

        buttonAddUserIfExists.setOnAction(e->handleAddFriend(boxUsername.getText()));
        buttonAddUserIfExists.setOnMouseEntered(e->Animations.mouseEnterTransition(buttonAddUserIfExists));
        buttonAddUserIfExists.setOnMouseExited(e->Animations.mouseExitTransition(buttonAddUserIfExists));

        buttonOkayIfNotExists.setOnAction(e->handleOkay());
        buttonOkayIfNotExists.setOnMouseEntered(e->Animations.mouseEnterTransition(buttonOkayIfNotExists));
        buttonOkayIfNotExists.setOnMouseExited(e->Animations.mouseExitTransition(buttonOkayIfNotExists));

        buttonBack.setOnAction(e->handleReturn((Node) e.getSource()));
        buttonBack.setOnMouseEntered(e->Animations.mouseEnterTransition(buttonBack));
        buttonBack.setOnMouseExited(e->Animations.mouseExitTransition(buttonBack));
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

    private void handleAddFriend(String text) {
        // code here to add a user as a friend iff exists
    }

    private boolean handleSearchUser(String username){
        // write code here to find user in database
        // currently returning true only for checking workflow
        return true;
    }
}
