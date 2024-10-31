package com.tankwars.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartGameController {

    @FXML
    private Button buttonPlayWithFriend;
    @FXML
    private Button buttonPlayWithComputer;
    @FXML
    private Button buttonReturn;

    @FXML
    public void initialize(){
        buttonPlayWithFriend.setOnAction(e->{
            handlePlayWithFriend();
        });

        buttonPlayWithComputer.setOnAction(e->handlePlayWithComputer());
        buttonReturn.setOnAction(e->handleReturn((Node) e.getSource()));
    }

    private void handleReturn(Node source) {
        Stage sourceStage = (Stage) source.getScene().getWindow();
        if(sourceStage!=null)
            sourceStage.close();
    }

    private void handlePlayWithComputer() {
        //code here for play with computer functionality
    }

    private void handlePlayWithFriend() {
        //code here for play with friend functionality
    }
}