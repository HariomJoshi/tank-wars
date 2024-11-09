package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.Animations;
import com.tankwars.frontend.tankwarsclient.InitializeGame;
import com.tankwars.frontend.utils.User;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DashboardMainController {
    @FXML
    private Button buttonGameInvites;
    @FXML
    private Button buttonPlayWithComputer;
    @FXML
    private Button buttonManageFriends;
    @FXML
    private Button buttonGameHistory;
    @FXML
    private Button buttonExitGame;
    @FXML
    private Text headerDashboard;


    private final List<Button> dashboardActions = new ArrayList<>();
    private ListView<Invite> listGameInvites = new ListView<>();
    private Popup invites = new Popup();
    private List<Invite> currentInvites = new ArrayList<>(); // List to hold current invites
    private InitializeGame mainApp;

    public void initialize() {
        Animations.waveAnimation(headerDashboard);

        dashboardActions.add(buttonPlayWithComputer);
        dashboardActions.add(buttonManageFriends);
        dashboardActions.add(buttonGameHistory);
        dashboardActions.add(buttonExitGame);

        for (Button btn : dashboardActions) {
            btn.setOnMouseEntered(e -> Animations.mouseEnterTransition(btn));
            btn.setOnMouseExited(e -> Animations.mouseExitTransition(btn));
        }

        buttonPlayWithComputer.setOnAction(e -> {
            try {
                handlePlayWithComputer();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonManageFriends.setOnAction(e -> {
            try {
                handleManageFriends();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonGameHistory.setOnAction(e -> handleGameHistory());
        buttonExitGame.setOnAction(e -> handleExitGame());
        buttonGameInvites.setOnAction(e -> clickGameInvites());

        // Dummy data for checking invite section
        for (int i = 1; i <= 30; i++) {
            currentInvites.add(new Invite("User " + i)); // Populate currentInvites
        }
        updateInvites(); // Initial population of the list view

        listGameInvites.setCellFactory(lv -> new InviteCell());
        listGameInvites.setPrefHeight(400);
        listGameInvites.setPrefWidth(332);
        invites.getContent().add(listGameInvites);
    }

    private void clickGameInvites() {
        if (!invites.isShowing()) {
            // Calculate button's position on the screen
            double buttonX = buttonGameInvites.localToScreen(buttonGameInvites.getBoundsInLocal()).getMinX();
            double buttonY = buttonGameInvites.localToScreen(buttonGameInvites.getBoundsInLocal()).getMinY();

            // Position popup above the button
            invites.show(buttonGameInvites, buttonX, buttonY - listGameInvites.getPrefHeight());
        } else {
            invites.hide();
        }
    }

    private void updateInvites() {
        listGameInvites.getItems().clear(); // Clear current items
        listGameInvites.getItems().addAll(currentInvites); // Add updated list of invites
    }

    private void handleGameHistory() {
        // Implement game history handling logic here
    }

    private void handleManageFriends() throws IOException {
        // Implement friend management logic here
        Stage manageFriendsStage = new Stage();
        FXMLLoader manageFriendsLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/manage-friends.fxml"));
        Scene scene = new Scene(manageFriendsLoader.load());
        manageFriendsStage.setScene(scene);
        manageFriendsStage.initStyle(StageStyle.UNDECORATED);
        manageFriendsStage.initModality(Modality.APPLICATION_MODAL);
        manageFriendsStage.show();
    }

    private void handlePlayWithComputer() throws IOException {
        // Implement play with computer logic here
        mainApp.showGameWindow();
    }

    private void handleExitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> responseAlertBox = alert.showAndWait();
        if (responseAlertBox.isPresent() && responseAlertBox.get() == ButtonType.YES)
            System.exit(1);
    }

    public void setMainApp(InitializeGame game) {
        // Implement the logic to set the main app instance if necessary
        mainApp = game;
    }







    // Custom cell for displaying invites
    class InviteCell extends ListCell<Invite> {
        @Override
        protected void updateItem(Invite invite, boolean empty) {
            super.updateItem(invite, empty);

            if (empty || invite == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Create the layout for the cell
                HBox hbox = new HBox(10); // 10px spacing

                // Create a Text node for the username
                Text usernameText = new Text(invite.getUsername());
                usernameText.setWrappingWidth(150); // Set max width for the text
                usernameText.setTextAlignment(TextAlignment.LEFT);
                usernameText.setStyle("-fx-ellipsis-string: '...';"); // Add ellipsis for overflow

                // Create buttons with fixed width
                Button acceptButton = new Button("Accept");
                Button rejectButton = new Button("Reject");
                acceptButton.setPrefWidth(70); // Fixed width for the accept button
                rejectButton.setPrefWidth(70); // Fixed width for the reject button

                // Set actions for the buttons
                acceptButton.setOnAction(event -> {
                    // Logic to handle acceptance
//                    System.out.println(invite.getUsername() + " accepted.");
                    currentInvites.remove(invite); // Remove invite from the list
                    updateInvites(); // Update the ListView
                });

                rejectButton.setOnAction(event -> {
                    // Logic to handle rejection
//                    System.out.println(invite.getUsername() + " rejected.");
                    currentInvites.remove(invite); // Remove invite from the list
                    updateInvites(); // Update the ListView
                });

                // Add components to the layout
                hbox.getChildren().addAll(usernameText, acceptButton, rejectButton);
                setGraphic(hbox);
            }
        }
    }



    // Invite class to represent game invites
    class Invite {
        private String username;

        public Invite(String username) {
            this.username = username;
        }

        public String getUsername() {
            return this.username;
        }

        @Override
        public String toString() {
            return username; // Display only the username in the ListView
        }
    }
}
