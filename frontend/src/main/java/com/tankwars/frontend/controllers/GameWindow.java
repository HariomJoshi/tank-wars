package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.InitializeGame;
import com.tankwars.frontend.tankwarsclient.Tank;
import com.tankwars.frontend.tankwarsclient.models.GameState;
import com.tankwars.frontend.tankwarsclient.models.GameUser;
import com.tankwars.frontend.tankwarsclient.models.TwoPlayers;
import com.tankwars.frontend.tankwarsclient.terrains.DesertTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.GrassMountainTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.SnowyMountainTerrain;
import com.tankwars.frontend.tankwarsclient.terrains.Terrain;
import com.tankwars.frontend.utils.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Random;

public class GameWindow {
    @FXML
    private BorderPane mainPane;
    @FXML
    private StackPane terrainAndControlsPane;

    private Label labelHealthPlayer1;
    private Label labelHealthPlayer2;
    InitializeGame mainApp;

    Tank userTank = new Tank(0, 0, Color.RED, Color.GREEN, false);
    Tank opponentTank = new Tank(0, 0, Color.YELLOW, Color.GREEN, true);

    double[] terrainHeights;

    // Create and add GameController


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
        terrainHeights = terrain.getTerrainHeights();

        // setting up tanks
        // Set up tanks with positioning from the bottom of the canvas
//        double userTankX = 500  ;
//        double opponentTankX = 767.5;
//        double canvasHeight = gameWindowCanvas.getHeight();

        int sectionUser = 1;
        int sectionOpponent = 13;

        double[] heightUserPlace = terrain.placeObjectInSection(sectionUser,gc, userTank);
        double[] heightOpponentPlace = terrain.placeObjectInSection(sectionOpponent, gc, opponentTank);

        GameController gameController = new GameController( 1535, 865, heightUserPlace, heightOpponentPlace, this);
        System.out.println(heightUserPlace[0]+" "+heightUserPlace[1]+" ekecnjk");

        userTank.setTranslateX(heightUserPlace[0]);
        userTank.setTranslateY(heightUserPlace[1]-40);

        opponentTank.setTranslateX(heightOpponentPlace[0]);
        opponentTank.setTranslateY(heightOpponentPlace[1]-40);



// Calculate Y coordinates relative to the bottom of the canvas
//        double userTankY = getTerrainHeightAtX(terrain, sectionUser);
//        double opponentTankY = getTerrainHeightAtX(terrain, sectionOpponent);

// Update the positions of the tanks
//        userTank.setTranslateX(-userTankX);
//        userTank.setTranslateY(-userTankY); // Position relative to the bottom
//
//        opponentTank.setTranslateX(opponentTankX-267.5);
//        opponentTank.setTranslateY(-opponentTankY);

        labelHealthPlayer1 = new Label("Health : 100");
        labelHealthPlayer2 = new Label("Health : 100");

        labelHealthPlayer1.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial Black'; -fx-text-fill: #FF0000;");
        labelHealthPlayer2.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial Black'; -fx-text-fill: #FF0000;");


        mainPane.getChildren().addAll(userTank, opponentTank);

        terrainAndControlsPane.getChildren().addAll(gameController, labelHealthPlayer1, labelHealthPlayer2);
        // Set layout constraints for GameController
        StackPane.setAlignment(gameController, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setAlignment(labelHealthPlayer1, Pos.TOP_LEFT);
        StackPane.setAlignment(labelHealthPlayer2, Pos.TOP_RIGHT);
//        StackPane.setAlignment(userTank, Pos.BOTTOM_CENTER);
//        StackPane.setAlignment(opponentTank, Pos.BOTTOM_CENTER);
//        terrainAndControlsPane.getChildren().setAll(gameWindowCanvas, userTank, opponentTank, gameController);
//        gameController.requestFocus();

        // initializing game State

        GameState state = GameState.getInstance();
        opponentTank.setBarrelAngle(45);
        if(TwoPlayers.isTwoPlayerGame){
            state.setPlayer1(new GameUser(TwoPlayers.getInstance().getFirstUser(), userTank));
            state.setPlayer2(new GameUser(TwoPlayers.getInstance().getSecondUser(), opponentTank));
        }else{
//        state.setPlayer1(new GameUser(User.getInstance().getUsername(), userTank));
            state.setPlayer1(new GameUser("Aryan", userTank));
            state.setPlayer2(new GameUser("Computer", opponentTank));
        }
        state.setCurrentTurn(state.getPlayer1());

    }

    private double getTerrainHeightAtX(Terrain terrain, int posX) {
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

    public boolean terrainCollision(double x, double y){
//        double one_unit = (double)1535/Terrain.SECTIONS;
//        int index = (int) Math.ceil( x/one_unit);
        if(terrainHeights[(int)x] < 865- y) return true;
        return false;
    }

    public void updateHealth(){
        GameState state = GameState.getInstance();
        int value = state.getPlayer1().getHealth();
        labelHealthPlayer1.setText("Health : "+value);
        value = state.getPlayer2().getHealth();
        labelHealthPlayer2.setText("Health : "+value);
    }

}
