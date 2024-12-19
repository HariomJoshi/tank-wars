package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.Projectile;
import com.tankwars.frontend.tankwarsclient.Tank;
import com.tankwars.frontend.tankwarsclient.models.GameState;
import com.tankwars.frontend.tankwarsclient.weapons.CannonBall;
import com.tankwars.frontend.tankwarsclient.weapons.Napalm;
import com.tankwars.frontend.tankwarsclient.weapons.Weapon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.cert.PolicyNode;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GameController extends AnchorPane {

    private final int width;
    private final int height;
    private ChoiceBox<Weapon> weapons;
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

    private Tank user, opponent;
    private boolean isProjectileInMotion = false;
    private boolean gameEnded = false;
    private int currentTurn = 0;
    private ImageView circle;
    private Label turnLabel;

    private final double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    private final double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();

    private final double initialX, initialY;
    private GameWindow mainScreen;


    public GameController(int width, int height, double[] userCoordinates, double[] opponentCoordinates, GameWindow mainScreen) {
        this.initialX = userCoordinates[0];
        this.initialY = userCoordinates[1];
        initializeMainControls();
        initializeWeapons();
        initializeAngleControlsPanel();
        initializePowerControlsPanel();
        setupKeyControls();
        this.requestFocus(); // Request focus for key controls
        this.width = width;
        this.height = height;
        this.mainScreen = mainScreen;
        buttonFire.setOnAction(e->handleFireWeapon());
    }

    private void initializeWeapons() {
        weapons.getItems().add(new CannonBall("Cannon Ball", "Explosive", 5));
        weapons.getItems().add(new Napalm("Napalm", "Fire", 1));

        // Set a default selection
        weapons.setValue(weapons.getItems().get(0));
        weapons.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            labelWeightValue.setText(newValue.getWeight()+"");
            this.weight = newValue.getWeight();
            this.damage = newValue.getDamage();
        }));
    }

    private void initializeMainControls() {
        // Main control container
        mainControls = new HBox();
        mainControls.setPrefHeight(100);
        mainControls.setMaxHeight(100);
        mainControls.setAlignment(Pos.CENTER);
        mainControls.setSpacing(35);
        mainControls.setStyle("-fx-background-color: black;");

        // Create the ChoiceBox for weapons
        weapons = new ChoiceBox<>();
        weapons.setPrefHeight(35.0);
        weapons.setPrefWidth(250.0);
        weapons.setStyle("-fx-background-color: yellowgreen;");

        turnLabel = new Label("Player 1's Turn"); // Start with Player 1's turn
        turnLabel.setFont(new Font("Algerian", 22.0));
        turnLabel.setTextFill(Color.YELLOW);

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

        mainControls.getChildren().addAll(turnLabel, weapons, weightBox, buttonFire, buttonAngle, buttonPower, buttonExit);
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
//            System.exit(1);
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
            state.getPlayer1().getTank().setBarrelAngle(angle);
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
                state.getPlayer1().getTank().setBarrelAngle(angle);
            } else if (event.getCode() == KeyCode.LEFT) {
                angle = Math.max(angle - 1, 0); // Decrease angle
                labelAngle.setText(String.valueOf(angle));
                state.getPlayer1().getTank().setBarrelAngle(angle);
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
        System.out.println("Fire button working");
        freezeControls();
//        unFreezeControls();
        fireProjectile();
    }

    private void fireProjectile() {
        if(isProjectileInMotion || gameEnded){
            return; // Prevent firing if a projectile is already in motion or the game has ended
        }

        try {
            if(GameState.getInstance().getCurrentTurn().getName()!="Computer") {
                fireTankProjectile(angle, 2*power, weight);

                switchTurns();
            }

        }
        catch (NumberFormatException e){
            System.out.println("Please enter valid numerical values");
        }
    }

    public void fireTankProjectile(double angle, double power, double weight){
        if(GameState.getInstance().getCurrentTurn().getName()=="Computer")
            visualizeProjectile(angle, power, weight, GameState.getInstance().getPlayer2().getTank().getX(),
                    GameState.getInstance().getPlayer2().getTank().getY());
        else
            visualizeProjectile(angle, power, weight, GameState.getInstance().getPlayer1().getTank().getX(),
                    GameState.getInstance().getPlayer1().getTank().getY());
    }

    private void visualizeProjectile(double angle, double power, double weight, double initialX, double initialY) {
        isProjectileInMotion = true;
        circle = new ImageView(weapons.getValue().getIcon());
        circle.setFitWidth(22);
        circle.setFitHeight(22);

        // Correct initial position of the projectile
//        double initialX = -user.getTranslateX();
//        double initialY = -user.getTranslateY();  // Adjusted for screen origin
        // Set projectile's initial position to the tank's position
        System.out.println(initialX+" "+initialY+" in game cnt");
        circle.setLayoutX(initialX);
        circle.setLayoutY(initialY);
        getChildren().add(circle);

        Projectile projectile = new Projectile(angle, power, weight, initialX, 895-initialY, width, height);
        List<Point2D> path = projectile.calculateTrajectoryPoints(projectile);
        System.out.println(path.size());

        Timeline timeline = new Timeline();
        int temp = -1;
        for (int i = 0; i < path.size(); i++) {
            Point2D point = path.get(i);
            if(mainScreen.terrainCollision(point.getX(), point.getY())){
                temp = i;
                break;
            }
            final int idx = i;
            List<Point2D> finalPath = path;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 0.02), e -> {
                if (circle != null) {
                    circle.setLayoutX(point.getX());
                    circle.setLayoutY(screenHeight - point.getY());  // Adjusting Y-coordinate for screen origin
                }
                if (idx == finalPath.size() - 1) {
                    cleanupProjectile(point);
                    checkHit(point);
                }
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.setCycleCount(1);
        int finalTemp = temp;
        timeline.setOnFinished(e -> cleanupProjectile((finalTemp ==-1)?path.getLast():path.get(finalTemp)));
        timeline.play();
    }


    private void cleanupProjectile(Point2D point) {
        if (circle != null) {
            getChildren().remove(circle);
            circle = null;
        }
        isProjectileInMotion = false;

        // Get the width and height of the game window (stage)
        double windowWidth = ((Stage) getScene().getWindow()).getWidth();
        double windowHeight = ((Stage) getScene().getWindow()).getHeight();

        // Check if the projectile is still within bounds
        if (point.getX() >= 0 && point.getX() < windowWidth && point.getY() >= 0 && point.getY() < windowHeight) {
            if (!gameEnded) {
                weapons.getValue().playAnimation((Stage) getScene().getWindow(), point.getX(), 865-point.getY());
                switchTurns(); // Switch turns if the game hasn't ended
//                state.switchTurns();
                subtractPoints(weapons.getValue(), point.getX(), 865-point.getY());
            }
        }
    }
    GameState state = GameState.getInstance();
    private void subtractPoints(Weapon weapon, double X, double Y){
        // check if collision occurs
        // check for user1
        Tank tempTank = state.getPlayer1().getTank();
        double tempX = tempTank.getX();
        double tempY = tempTank.getY();
        if((tempX+50 > X && tempX-50 < X)){
            state.getPlayer1().decreaseHealth(weapon.getDamage());
        }

        tempTank = state.getPlayer2().getTank();
        tempX = tempTank.getX();
        tempY = tempTank.getY();
        if((tempX+50 > X && tempX-50 < X)){
            state.getPlayer2().decreaseHealth(weapon.getDamage());
        }

        mainScreen.updateHealth();
    }


    private void switchTurns() {
        if (gameEnded) return;

        state.switchTurns();
        turnLabel.setText(state.getCurrentTurn().getName() + "'s" + " Turn");

        if (GameState.getInstance().getCurrentTurn().getName()=="Computer" && !gameEnded) {
            Timeline computerFireDelay = new Timeline(new KeyFrame(Duration.seconds(2), e -> fireComputerProjectile()));
            computerFireDelay.setCycleCount(1);
            computerFireDelay.play();
        }
    }


    private void fireComputerProjectile() {
        if(isProjectileInMotion || gameEnded){
            return;
        }

        if(GameState.getInstance().getCurrentTurn().getName()=="Computer") {
            System.out.println("inside calling firecomputer");
            Random random = new Random();
            double angle = random.nextDouble() * 45 + 105;  // Random angle between 105 and 150 degrees
            double power = random.nextDouble() * 80 + 70; // Random power between 70 and 150
            double weight = random.nextDouble() * 1 + 1; // Random weight between 1 and 2

            // Fire the computer's projectile
            fireTankProjectile(angle, power, weight);
            unFreezeControls();
            state.switchTurns();
//
//            switchTurns();
        }
    }




    private  void checkHit(Point2D point) {
        // Check if the projectile hits the player's tank or computer's tank

        if(gameEnded){
            return;
        }


        if(currentTurn == 0) {  // User's Turn
            if (point.getX() >= opponent.getTranslateX() && point.getX() <= opponent.getTranslateX() + 60 &&
                    point.getY() >= opponent.getTranslateY() && point.getY() <= opponent.getTranslateY() + opponent.getHeight()) {
                System.out.println("Player wins! Hit the computer's tank.");
                gameEnded = true;
            }
        }
        else{  //Computer's turn
            if (point.getX() >= user.getTranslateX() && point.getX() <= user.getTranslateX() + 60 &&
                    point.getY() >= user.getTranslateY() && point.getY() <= user.getTranslateY() + user.getHeight()) {
                System.out.println("Computer wins! Hit the player's tank.");
                gameEnded = true;
            }
        }
        if(gameEnded) {
            cleanupProjectile(point);  // Stop any projectile in motion
            System.out.println("Game Over");
        }
    }

}
