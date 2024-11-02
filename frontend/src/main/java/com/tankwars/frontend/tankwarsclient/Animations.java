package com.tankwars.frontend.tankwarsclient;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Animations {
    public static void flipTransition(Node currentPane, Node nextPane) {
        // Rotate current pane to 90 degrees to hide

        RotateTransition hideCurrent = new RotateTransition(Duration.millis(500), currentPane);
        hideCurrent.setAxis(Rotate.Y_AXIS); // Flip on the Y-axis
        hideCurrent.setFromAngle(0);
        hideCurrent.setToAngle(90);
        hideCurrent.setOnFinished(event -> {
            currentPane.setVisible(false);
            nextPane.setVisible(true);

            // Reset the next pane to start from a 90-degree angle
            nextPane.setRotate(-90);

            // Rotate the next pane back to 0 degrees to show
            RotateTransition showNext = new RotateTransition(Duration.millis(500), nextPane);
            showNext.setAxis(Rotate.Y_AXIS); // Flip on the Y-axis
            showNext.setFromAngle(-90);
            showNext.setToAngle(0);
            showNext.play();
        });

        hideCurrent.play();
    }

    public static void fadeScenes(Stage stage, Scene currScene, Scene nextScene) {
        Pane currRoot = (Pane) currScene.getRoot();
        Pane nextRoot = (Pane) nextScene.getRoot();

        // Ensure the next scene root is initially invisible
        nextRoot.setOpacity(0.0);

        // Create fade-out transition for the current scene
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), currRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Create fade-in transition for the next scene
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), nextRoot);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Set the action after fade-out is finished
        fadeOut.setOnFinished(event -> {
            // Switch to the next scene
            stage.setScene(nextScene);
            // Start the fade-in transition
            fadeIn.play();
        });

        // Start the fade-out transition
        fadeOut.play();
    }

    public static void waveAnimation(Text text) {
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

    public static void mouseEnterTransition(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
    }

    public static void mouseExitTransition(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }
}
