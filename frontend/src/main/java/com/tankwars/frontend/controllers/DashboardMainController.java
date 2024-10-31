package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.InitializeGame;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DashboardMainController {

    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonFriendRequests;
    @FXML
    private Button buttonAddFriend;
    @FXML
    private Button buttonGameHistory;
    @FXML
    private Button buttonExitGame;

    private List<Button> dashboardButtons = new ArrayList<>();

    @FXML
    private Text headerDashboard;


    public void initialize() {
        addWaveAnimation(headerDashboard);

        dashboardButtons.add(buttonStart);
        dashboardButtons.add(buttonAddFriend);
        dashboardButtons.add(buttonFriendRequests);
        dashboardButtons.add(buttonGameHistory);
        dashboardButtons.add(buttonExitGame);

        for(Button button : dashboardButtons){
            button.setOnMouseEntered(e->mouseEnterTransition(button));
            button.setOnMouseExited(e->mouseExitTransition(button));
        }

        buttonExitGame.setOnAction(e->handleExitGame());
        buttonStart.setOnAction(e->handleStartGame());
        buttonAddFriend.setOnAction(e-> {
            try {
                handleAddFriend();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void handleAddFriend() throws IOException {
        Stage addFriend = new Stage();
        FXMLLoader addFriendLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/add-friend.fxml"));
        Scene scene = new Scene(addFriendLoader.load());
        addFriend.setScene(scene);
        addFriend.initStyle(StageStyle.UNDECORATED);
        addFriend.initModality(Modality.APPLICATION_MODAL);
        addFriend.show();
    }

    private void handleStartGame() {
        try{
            Stage gameOptionsStage = new Stage();
            FXMLLoader gameOptionsLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/start-game-options.fxml"));
            Parent root = gameOptionsLoader.load();
            Scene scene = new Scene(root);
            gameOptionsStage.setScene(scene);
            gameOptionsStage.initStyle(StageStyle.UNDECORATED);
            gameOptionsStage.initModality(Modality.APPLICATION_MODAL);
            gameOptionsStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleExitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> responseAlertBox = alert.showAndWait();
        if(responseAlertBox.isPresent() && responseAlertBox.get()==ButtonType.YES)
            System.exit(1);
    }

    private void addWaveAnimation(Text text) {
        // Set up the timeline for the wave animation
        Timeline timeline = new Timeline();
        final double amplitude = 20; // Adjust amplitude as needed
        final double frequency = 0.5; // Adjust frequency for speed of wave

        for (int i = 0; i < 360; i += 10) { // Creates keyframes at every 10-degree increment
            int angle = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(angle * 10), e -> {
                // Move text up and down in a sine wave pattern
                text.setTranslateY(Math.sin(Math.toRadians(angle)) * amplitude);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setCycleCount(Timeline.INDEFINITE); // Loop the animation
        timeline.play();
    }

    private void mouseEnterTransition(Button button){
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
    }

    private void mouseExitTransition(Button button){
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }

    public void setMainApp(InitializeGame game) {

    }
}