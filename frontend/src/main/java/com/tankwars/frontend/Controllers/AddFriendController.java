package com.tankwars.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
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

    @FXML
    public void initialize(){
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
        buttonOkayIfNotExists.setOnAction(e->handleOkay());
        buttonBack.setOnAction(e->handleReturn((Node) e.getSource()));
    }

    private void handleReturn(Node node) {
        // Get the currently active stage (topmost)
        Stage currentStage = (Stage) node.getScene().getWindow(); // Replace someNode with a reference to a node in the current scene

        if (currentStage != null) {
            currentStage.close(); // Close the current stage
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
