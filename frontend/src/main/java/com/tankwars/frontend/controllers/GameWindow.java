package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.InitializeGame;
import com.tankwars.frontend.tankwarsclient.Tank;
import com.tankwars.frontend.tankwarsclient.terrains.DesertTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.GrassMountainTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.SnowyMountainTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.Terrain;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class GameWindow {
    @FXML
    private BorderPane mainPane;
    @FXML
    private StackPane terrainAndControlsPane;

    InitializeGame mainApp = new InitializeGame();

    Tank userTank = new Tank(0, 0, Color.RED, Color.GREEN, false);
    Tank opponentTank = new Tank(0, 0, Color.YELLOW, Color.GREEN, true);

    // Create and add GameController
    GameController gameController = new GameController(userTank, (Stage)mainPane.getScene().getWindow());

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

        // setting up tanks
        // Set up tanks with positioning from the bottom of the canvas
        double userTankX = 500  ;
        double opponentTankX = 767.5;
        double canvasHeight = gameWindowCanvas.getHeight();

// Calculate Y coordinates relative to the bottom of the canvas
        double userTankY = getTerrainHeightAtX(terrain, userTankX);
        double opponentTankY = getTerrainHeightAtX(terrain, opponentTankX);

// Update the positions of the tanks
        userTank.setTranslateX(-userTankX);
        userTank.setTranslateY(-userTankY); // Position relative to the bottom

        opponentTank.setTranslateX(opponentTankX-500);
        opponentTank.setTranslateY(-opponentTankY);



        terrainAndControlsPane.getChildren().addAll(userTank, opponentTank);
        terrainAndControlsPane.getChildren().add(gameController);
        // Set layout constraints for GameController
        StackPane.setAlignment(gameController, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setAlignment(userTank, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(opponentTank, Pos.BOTTOM_CENTER);
//        terrainAndControlsPane.getChildren().setAll(gameWindowCanvas, userTank, opponentTank, gameController);
//        gameController.requestFocus();
    }

    private double getTerrainHeightAtX(Terrain terrain, double posX) {
        return terrain.getHeightAt(posX);
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
        this.mainApp = gameWindow;
    }
}