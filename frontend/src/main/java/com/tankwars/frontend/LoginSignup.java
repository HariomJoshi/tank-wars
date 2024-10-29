package com.tankwars.frontend;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class LoginSignup extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUpInitialWindow(primaryStage);
    }

    private void setUpInitialWindow(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignup.class.getResource("initial-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1127, 727);
        stage.setTitle("Tank Wars");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(false);
        stage.initStyle(StageStyle.DECORATED);
        stage.show();

        // Get reference to the Text element
        Text welcomeText = (Text) scene.lookup("#welcomeText");

        // Create Scale Transition for the text
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), welcomeText);
        scaleTransition.setFromX(0.0);
        scaleTransition.setFromY(0.0);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        // Create Fade Transition for the scene
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), scene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Create a SequentialTransition
        SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition);
        // Add a pause after the scale transition
        sequentialTransition.getChildren().add(new javafx.animation.PauseTransition(Duration.seconds(1)));
        // Add the fade transition to the sequence
        sequentialTransition.getChildren().add(fadeOut);

        // Play the entire sequential transition
        sequentialTransition.setOnFinished(event -> {
            try {
                showLoginWindow(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sequentialTransition.play();
    }

    private void showLoginWindow(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-signup-scene.fxml"));
        // making a loader from an fxml file
        Scene loginScene = new Scene(fxmlLoader.load());
        // making a new scene from fxml file

        // Apply Fade Transition to login scene
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), loginScene.getRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        stage.setScene(loginScene);
        stage.setMaximized(false);
        fadeIn.play();
    }
}
