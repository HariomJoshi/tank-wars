package com.tankwars.frontend.controllers;


//import com.tankwars.frontend.tankwarsclient.Projectile;
import com.tankwars.frontend.tankwarsclient.Tank;
//import com.tankwars.frontend.tankwarsclient.weapons.CannonBall;
//import com.tankwars.frontend.tankwarsclient.weapons.Napalm;
//import com.tankwars.frontend.tankwarsclient.weapons.Weapon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;

import javafx.application.Platform;
import javafx.geometry.Insets;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

import java.util.Optional;

public class GameController extends AnchorPane {


    private final Stage source;

    private ChoiceBox<String> weapons;
    private Label labelWeightValue;
    private Button buttonFire;
    private Label labelAngle;
    private Button buttonAngle, buttonExit;
    private Label labelPower;
    private Button buttonPower;


    private int angle, power, weight, damage;

    private HBox mainControls;      // Original controls panel
    private HBox angleControlsPanel; // Angle controls panel
    private HBox powerControlsPanel; // Power controls panel


    private Tank user;
    private boolean isProjectileInMotion = false;
    private boolean gameEnded = false;
    private int currentTurn = 0;

    public GameController(Tank user, Stage source) {
        initializeMainControls();
//        initializeWeapons();

        initializeAngleControlsPanel();
        initializePowerControlsPanel();
        setupKeyControls();
        this.requestFocus(); // Request focus for key controls

        this.user = user;
        this.source = source;
        buttonFire.setOnAction(e->handleFireWeapon());
    }

//    private void initializeWeapons() {
//        // Dynamically add all subclasses of Weapon to the ChoiceBox
//        List<Class<? extends Weapon>> weaponClasses = Arrays.asList(CannonBall.class, Napalm.class);
//
//        // Add instances of each subclass to the ChoiceBox
//        for (Class<? extends Weapon> weaponClass : weaponClasses) {
//            try {
//                Weapon weapon = weaponClass.getDeclaredConstructor().newInstance();
//                weapons.getItems().add(weapon);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // Set a default selection
//        weapons.setValue(weapons.getItems().get(0));
//        weapons.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
//            labelWeightValue.setText(newValue.getWeight()+"");
//            this.weight = newValue.getWeight();
//            this.damage = newValue.getDamage();
//        }));
//    }


    private void initializeMainControls() {
        // Main control container
        mainControls = new HBox();
        mainControls.setPrefHeight(100);
        mainControls.setMaxHeight(100);
        mainControls.setAlignment(Pos.CENTER);
        mainControls.setSpacing(100);
        mainControls.setStyle("-fx-background-color: black;");

        // Create the ChoiceBox for weapons
        weapons = new ChoiceBox<>();
        weapons.setPrefHeight(35.0);
        weapons.setPrefWidth(250.0);
        weapons.setStyle("-fx-background-color: yellowgreen;");

        // Weight display
        VBox weightBox = new VBox();
        weightBox.setAlignment(Pos.CENTER);
        Label labelWeight = new Label("Weight");
        labelWeight.setFont(new Font("Algerian", 22.0));
        labelWeight.setTextFill(createGradient());

        labelWeightValue = new Label("0");
        labelWeightValue.setFont(new Font("Algerian", 36.0));
        labelWeightValue.setTextFill(Color.web("#50c72f"));
        weightBox.getChildren().addAll(labelWeight, labelWeightValue);

        // Fire button
        buttonFire = new Button("Fire");
        buttonFire.setPrefHeight(42.0);
        buttonFire.setPrefWidth(120.0);
        buttonFire.setStyle("-fx-background-color: yellowgreen;");
        buttonFire.setFont(new Font("Algerian", 24.0));
        buttonFire.setFocusTraversable(false); // Prevent focus on button

        // Angle button
        buttonAngle = new Button("Angle");
        labelAngle = new Label("0");
        labelAngle.setFont(new Font("Algerian", 36.0));
        labelAngle.setPadding(new Insets(0, 30, 0, 0));
        buttonAngle.setGraphic(labelAngle);
        buttonAngle.setPrefHeight(42.0);
        buttonAngle.setPrefWidth(198.0);
        buttonAngle.setFont(new Font("Algerian", 22.0));
        buttonAngle.setFocusTraversable(false); // Prevent focus on button

        buttonAngle.setOnAction(e -> toggleToAngleControls()); // Toggle to angle controls on click

        // Power button (with toggle functionality)
        buttonPower = new Button("Power");
        labelPower = new Label("0");
        labelPower.setFont(new Font("Algerian", 36.0));
        labelPower.setPadding(new Insets(0, 30, 0, 0));
        buttonPower.setGraphic(labelPower);
        buttonPower.setPrefHeight(42.0);
        buttonPower.setPrefWidth(198.0);
        buttonPower.setFont(new Font("Algerian", 22.0));
        buttonPower.setFocusTraversable(false); // Prevent focus on button

        buttonPower.setOnAction(e -> toggleToPowerControls()); // Toggle to power controls on click

        // Exit button
        buttonExit = new Button("Exit Game");
        buttonExit.setPrefHeight(42.0);
        buttonExit.setPrefWidth(120.0);
        buttonExit.setStyle("-fx-background-color: red;"); // Style the button differently
        buttonExit.setFont(new Font("Algerian", 24.0));
        buttonExit.setFocusTraversable(false); // Prevent focus on button
        buttonExit.setOnAction(e -> exitGame()); // Add action for exiting the game

        mainControls.getChildren().addAll(weapons, weightBox, buttonFire, buttonAngle, buttonPower, buttonExit);
        this.getChildren().add(mainControls);

        AnchorPane.setBottomAnchor(mainControls, 0.0);
        AnchorPane.setLeftAnchor(mainControls, 0.0);
        AnchorPane.setRightAnchor(mainControls, 0.0);
    }


    private void exitGame(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Confirm exit" ,ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> response = alert.showAndWait();
        if(response.isPresent() && response.get()==ButtonType.OK){
            Platform.exit();

           System.exit(1);

        }
    }

    private void initializeAngleControlsPanel() {
        // Angle controls panel with slider and okay button
        angleControlsPanel = new HBox();
        angleControlsPanel.setAlignment(Pos.CENTER);
        angleControlsPanel.setSpacing(90.0);
        angleControlsPanel.setStyle("-fx-background-color: black;");
        angleControlsPanel.setPrefHeight(156.0);
        angleControlsPanel.setPrefWidth(1519.0);
        angleControlsPanel.setVisible(false); // Initially hidden

        // Slider for Angle
        Slider angleSlider = new Slider(0, 180, 45); // Assuming angle is between 0 and 90 degrees
        angleSlider.setPrefWidth(755.0);
        angleSlider.setShowTickMarks(true);
        angleSlider.setShowTickLabels(true);
        angleSlider.setMajorTickUnit(10);
        angleSlider.setMinorTickCount(4);
        angleSlider.setBlockIncrement(1.0);
        angleSlider.setStyle("-fx-background-color: gray;");
        angleSlider.setCursor(javafx.scene.Cursor.HAND);
        angleSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            angle = newVal.intValue();
            labelAngle.setText(String.valueOf(angle)); // Update angle label
        });

        // Okay button for Angle
        Button okayButtonAngle = new Button("Okay");
        okayButtonAngle.setFont(new Font("Algerian", 24.0));
        okayButtonAngle.setStyle("-fx-background-color: yellowgreen;");
        okayButtonAngle.setPrefHeight(44.0);
        okayButtonAngle.setPrefWidth(198.0);
        okayButtonAngle.setCursor(javafx.scene.Cursor.HAND);
        okayButtonAngle.setFocusTraversable(false); // Prevent focus on button
        okayButtonAngle.setOnAction(e -> toggleToMainControls()); // Toggle back to main controls

        angleControlsPanel.getChildren().addAll(angleSlider, okayButtonAngle);
        this.getChildren().add(angleControlsPanel);

        AnchorPane.setBottomAnchor(angleControlsPanel, 0.0);
        AnchorPane.setLeftAnchor(angleControlsPanel, 0.0);
        AnchorPane.setRightAnchor(angleControlsPanel, 0.0);
    }

    private void initializePowerControlsPanel() {
        // Power controls panel with slider and okay button
        powerControlsPanel = new HBox();
        powerControlsPanel.setAlignment(Pos.CENTER);
        powerControlsPanel.setSpacing(90.0);
        powerControlsPanel.setStyle("-fx-background-color: black;");
        powerControlsPanel.setPrefHeight(156.0);
        powerControlsPanel.setPrefWidth(1519.0);
        powerControlsPanel.setVisible(false); // Initially hidden

        // Slider for Power
        Slider powerSlider = new Slider(0, 100, 50);
        powerSlider.setPrefWidth(755.0);
        powerSlider.setShowTickMarks(true);
        powerSlider.setShowTickLabels(true);
        powerSlider.setMajorTickUnit(10);
        powerSlider.setMinorTickCount(4);
        powerSlider.setBlockIncrement(1.0);
        powerSlider.setStyle("-fx-background-color: gray;");
        powerSlider.setCursor(javafx.scene.Cursor.HAND);
        powerSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            power = newVal.intValue();
            labelPower.setText(String.valueOf(power)); // Update power label
        });

        // Okay button for Power
        Button okayButtonPower = new Button("Okay");
        okayButtonPower.setFont(new Font("Algerian", 24.0));
        okayButtonPower.setStyle("-fx-background-color: yellowgreen;");
        okayButtonPower.setPrefHeight(44.0);
        okayButtonPower.setPrefWidth(198.0);
        okayButtonPower.setCursor(javafx.scene.Cursor.HAND);
        okayButtonPower.setFocusTraversable(false); // Prevent focus on button
        okayButtonPower.setOnAction(e -> toggleToMainControls()); // Toggle back to main controls

        powerControlsPanel.getChildren().addAll(powerSlider, okayButtonPower);
        this.getChildren().add(powerControlsPanel);

        AnchorPane.setBottomAnchor(powerControlsPanel, 0.0);
        AnchorPane.setLeftAnchor(powerControlsPanel, 0.0);
        AnchorPane.setRightAnchor(powerControlsPanel, 0.0);
    }

    private void toggleToAngleControls() {
        mainControls.setVisible(false);
        powerControlsPanel.setVisible(false);
        angleControlsPanel.setVisible(true); // Show angle controls
        this.requestFocus(); // Ensure GameController has focus
    }

    private void toggleToPowerControls() {
        mainControls.setVisible(false);
        angleControlsPanel.setVisible(false);
        powerControlsPanel.setVisible(true); // Show power controls
        this.requestFocus(); // Ensure GameController has focus
    }

    private void toggleToMainControls() {
        angleControlsPanel.setVisible(false);
        powerControlsPanel.setVisible(false);
        mainControls.setVisible(true); // Show main controls
        this.requestFocus(); // Ensure GameController has focus
    }

    private LinearGradient createGradient() {
        Stop[] stops = new Stop[]{
                new Stop(0, Color.rgb(110, 17, 17)),
                new Stop(1, Color.WHITE)
        };
        return new LinearGradient(0.757, 0.734, 0.757, 0.306, false, null, stops);
    }

    private void setupKeyControls() {
        // Add key event handler directly to this GameController
        this.setOnKeyPressed(event -> {
            // Key controls for angle adjustments
            if (event.getCode() == KeyCode.RIGHT) {
                angle = Math.min(angle + 1, 180); // Increase angle
                labelAngle.setText(String.valueOf(angle));
            } else if (event.getCode() == KeyCode.LEFT) {
                angle = Math.max(angle - 1, 0); // Decrease angle
                labelAngle.setText(String.valueOf(angle));
            } else if (event.getCode() == KeyCode.UP) {
                power = Math.min(power + 1, 100); // Increase power
                labelPower.setText(String.valueOf(power));
            } else if (event.getCode() == KeyCode.DOWN) {
                power = Math.max(power - 1, 0); // Decrease power
                labelPower.setText(String.valueOf(power));
            } else if(event.getCode()==KeyCode.S){
                handleFireWeapon();
            }
        });
    }

    public void freezeControls(){
        weapons.setDisable(true);
        buttonFire.setDisable(true);
        buttonAngle.setDisable(true);
        buttonPower.setDisable(true);
    }

    public void unFreezeControls(){
        weapons.setDisable(false);
        buttonFire.setDisable(false);
        buttonAngle.setDisable(false);
        buttonPower.setDisable(false);
    }

    private void handleFireWeapon() {

//        System.out.println("Fire button working");
//        freezeControls();
////        unFreezeControls();
//        Projectile projectile = new Projectile(angle, power, weight, user.getLayoutX(), user.getLayoutY(), source);
//        List<Point2D> path = projectile.calculateTrajectoryPoints(projectile);
    }
//
//    private void fireProjectile() {
//        if(isProjectileInMotion || gameEnded){
//            return; // Prevent firing if a projectile is already in motion or the game has ended
//        }
//
//        try {
//            if(currentTurn == 0) {
//                fireTankProjectile(angle, power, weight);
//
////                switchTurns();
//            }
//
//        }
//        catch (NumberFormatException e){
//            System.out.println("Please enter valid numerical values");
//        }
//    }
//
//    public void fireTankProjectile(double angle, double power, double weight){
//        visualizeProjectile(angle, power, weight);
//    }

}
