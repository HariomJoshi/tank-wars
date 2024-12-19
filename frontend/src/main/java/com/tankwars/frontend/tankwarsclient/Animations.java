package com.tankwars.frontend.tankwarsclient;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

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

    public static void cannonBallEffect(double x, double y, Scene scene) {
        int radius = 40;
        Circle circle = new Circle(radius);
        circle.setFill(Color.ORANGERED);
        circle.setLayoutX(x);
        circle.setLayoutY(y);

        // Scale transition for explosion effect
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), circle);
        scaleTransition.setFromX(0.0);
        scaleTransition.setFromY(0.0);
        scaleTransition.setToX(2.0); // Increase the scale for a larger explosion
        scaleTransition.setToY(2.0);

        // Fade transition for fading out effect
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.2), circle);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        // Add the circle to the root node of the scene
        if (scene.getRoot() instanceof Pane) { // Ensure root is a Pane or subclass
            Pane root = (Pane) scene.getRoot();
            root.getChildren().add(circle);
        }

        // Play both transitions in sequence
        scaleTransition.setOnFinished(e -> fadeTransition.play());
        fadeTransition.setOnFinished(e -> {
            // Remove circle from the scene after the animation
            if (scene.getRoot() instanceof Pane) {
                Pane root = (Pane) scene.getRoot();
                root.getChildren().remove(circle);
            }
        });

        scaleTransition.play();
    }

    public static void napalmStrikeEffect(double x, double y, Scene scene) {
        int explosionRadius = 15;  // Initial small explosion radius
        int flameRadius = 8;       // Smaller radius for flame particles
        int spreadRange = 200;     // Horizontal range over which flames will be scattered
        int numFlames = 28;        // Number of flame particles for spreading fire

        if (scene.getRoot() instanceof Pane) {
            Pane root = (Pane) scene.getRoot();

            // Initial small explosion effect
            Circle initialExplosion = new Circle(explosionRadius);
            initialExplosion.setFill(Color.rgb(255, 100, 0, 0.9)); // Bright orange-red
            initialExplosion.setLayoutX(x);
            initialExplosion.setLayoutY(y);

            // Scale and fade for initial explosion/bottle break effect
            ScaleTransition explosionScale = new ScaleTransition(Duration.seconds(0.3), initialExplosion);
            explosionScale.setFromX(1.0);
            explosionScale.setFromY(1.0);
            explosionScale.setToX(2.5);
            explosionScale.setToY(2.5);

            FadeTransition explosionFade = new FadeTransition(Duration.seconds(0.3), initialExplosion);
            explosionFade.setFromValue(1.0);
            explosionFade.setToValue(0.0);

            // Add the explosion circle to the root pane and play transitions
            root.getChildren().add(initialExplosion);
            explosionScale.setOnFinished(e -> explosionFade.play());
            explosionFade.setOnFinished(e -> root.getChildren().remove(initialExplosion));
            explosionScale.play();

            // Delay the flame spread to start after the explosion
            explosionFade.setOnFinished(e -> {
                Random rand = new Random();

                for (int i = 0; i < numFlames; i++) {
                    // Create each flame particle with random color variations for realism
                    Circle flame = new Circle(flameRadius);
                    flame.setFill(Color.rgb(255, rand.nextInt(150) + 50, 0, 0.7)); // Flickering orange-yellow

                    // Scatter flames within a horizontal range around (x, y)
                    double offsetX = rand.nextInt(spreadRange) - spreadRange / 2;
                    double offsetY = rand.nextInt(20) - 10; // Slight vertical variation
                    flame.setLayoutX(x + offsetX);
                    flame.setLayoutY(y + offsetY);

                    // Flickering scale transition for flame effect
                    ScaleTransition flicker = new ScaleTransition(Duration.seconds(0.4 + rand.nextDouble() * 0.3),
                            flame);
                    flicker.setFromX(1.0);
                    flicker.setFromY(1.0);
                    flicker.setToX(1.8 + rand.nextDouble() * 0.5);
                    flicker.setToY(1.8 + rand.nextDouble() * 0.5);
                    flicker.setCycleCount(6); // Multiple flickers
                    flicker.setAutoReverse(true);

                    // Fade transition for gradual disappearance
                    FadeTransition flameFade = new FadeTransition(Duration.seconds(3.0 + rand.nextDouble() * 0.5),
                            flame);
                    flameFade.setFromValue(0.8);
                    flameFade.setToValue(0.0);

                    // Add flame to the root pane and play animations
                    root.getChildren().add(flame);

                    // Start flickering, then fade out after flickering completes
                    flicker.setOnFinished(e2 -> flameFade.play());
                    flameFade.setOnFinished(e2 -> root.getChildren().remove(flame));

                    flicker.play();
                }
            });
        }
    }
}
