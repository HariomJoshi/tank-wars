package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.InitializeGame;
import com.tankwars.frontend.tankwarsclient.Tank;
import com.tankwars.frontend.tankwarsclient.models.GameState;
import com.tankwars.frontend.tankwarsclient.models.GameUser;
import com.tankwars.frontend.tankwarsclient.models.TwoPlayers;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TwoPlayersController {
    @FXML
    private Button buttonBack;
    @FXML
    private TextField boxPlayer1;
    @FXML
    private TextField boxPlayer2;
    @FXML
    private Button buttonStartMatch;

    private InitializeGame mainApp;

    @FXML
    public void initialize(){
        buttonBack.setOnAction(event -> handleReturn((Node) event.getSource()));
        buttonStartMatch.setOnAction(e-> {
            try {
                handleStartMatch();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void handleStartMatch() throws IOException {
        // logic here to start the match
        TwoPlayers.getInstance().setFirstUser(boxPlayer1.getText());
        TwoPlayers.getInstance().setSecondUser(boxPlayer2.getText());
//        mainApp.showGameWindow();
    }

    public void setMainApp(InitializeGame app){
        mainApp = app;
    }

    private void handleReturn(Node node) {
        // Get the currently active stage (topmost)
        Stage currentStage = (Stage) node.getScene().getWindow(); // Replace someNode with a reference to a node in the current scene

        if (currentStage != null) {
            currentStage.close(); // Close the current stage
        }
    }
}
