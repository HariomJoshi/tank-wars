package com.tankwars.frontend;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    public void start(Stage primaryStage) {
        try {
            setUpInitialWindow(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load initial window.");
        }
    }

    private void setUpInitialWindow(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignup.class.getResource("initial-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1127, 727);
        stage.setTitle("Tank Wars");
        stage.setScene(scene);
        stage.setResizable(false); // Assuming fixed size for the intro screen
        stage.initStyle(StageStyle.DECORATED);
        stage.show();

        // Get reference to the Text element (check for null)
        Text welcomeText = (Text) scene.lookup("#welcomeText");
        if (welcomeText != null) {
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

            // Combine transitions
            SequentialTransition sequentialTransition = new SequentialTransition(
                    scaleTransition,
                    new PauseTransition(Duration.seconds(1)),
                    fadeOut
            );

            // Handle end of transition sequence
            sequentialTransition.setOnFinished(event -> {
                try {
                    showLoginWindow(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to load login window.");
                }
            });
            sequentialTransition.play();
        } else {
            System.out.println("Warning: Text with ID 'welcomeText' not found.");
            showLoginWindow(stage);
        }
    }

    private void showLoginWindow(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-signup-scene.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());

        // Apply Fade Transition to login scene
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), loginScene.getRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        stage.setScene(loginScene);
        stage.setResizable(false); // Assuming fixed size for login/signup screen
        fadeIn.play();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
