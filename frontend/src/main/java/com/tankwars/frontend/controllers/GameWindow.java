package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.InitializeGame;
import com.tankwars.frontend.tankwarsclient.terrains.DesertTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.GrassMountainTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.SnowyMountainTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.Terrain;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.Random;

public class GameWindow {
    @FXML
    private BorderPane mainPane;
    @FXML
    private StackPane terrainAndControlsPane;

    private InitializeGame mainApp;
    // Create and add GameController
    GameController gameController = new GameController();

    @FXML
    public void initialize() {
        // Set up the game canvas
        Canvas gameWindowCanvas = new Canvas(1535, 865);
        terrainAndControlsPane.getChildren().add(gameWindowCanvas);
        GraphicsContext gc = gameWindowCanvas.getGraphicsContext2D();

        // Draw the terrain
        Terrain terrain = getRandomTerrain();
        terrain.drawSky(gc);
        terrain.drawTerrain(gc);

        terrainAndControlsPane.getChildren().add(gameController);

        // Set layout constraints for GameController
        StackPane.setAlignment(gameController, javafx.geometry.Pos.BOTTOM_CENTER);
//        gameController.requestFocus();
    }

    private Terrain getRandomTerrain() {
        Random random = new Random();
        int choice = random.nextInt(3); // Adjust range based on the number of subclasses
        return switch (choice) {
            case 0 -> new GrassMountainTerrain();
            case 1 -> new DesertTerrain();
            case 2 -> new SnowyMountainTerrain();
            default -> new GrassMountainTerrain(); // Fallback in case of error
        };
    }

    public void setMainApp(InitializeGame gameWindow) {
        mainApp = gameWindow;
    }
}
