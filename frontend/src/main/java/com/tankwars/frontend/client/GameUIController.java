package com.tankwars.frontend.client;


import com.tankwars.frontend.models.GameState;
import com.tankwars.frontend.utils.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GameUIController {

    // following must be replaced by actual buttons
    @FXML
    private TextField angleField;
    @FXML
    private TextField powerField;
    @FXML
    private Button fireButton;

    private GameState currentGameState = GameState.getInstance();


    public void updateUIWithProjectile(double x, double y) {
        Platform.runLater(() -> {
            // here you have to update position of ball
        });
    }

}

