package com.tankwars.frontend.tankwarsclient;

import com.tankwars.frontend.controllers.DashboardMainController;
import com.tankwars.frontend.controllers.LoginSignupController;
import com.tankwars.frontend.utils.User;
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

public class InitializeGame extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        setUpInitialWindow();
    }

    private User currentUser = User.getInstance();

    private void setUpInitialWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/initial-page.fxml"));
        Scene initialScene = new Scene(fxmlLoader.load(), 1127, 727);

        primaryStage.setTitle("Tank Wars");
        primaryStage.setScene(initialScene);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();

        // Handle transition animation
        Text welcomeText = (Text) initialScene.lookup("#welcomeText");
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), welcomeText);
        scaleTransition.setFromX(0.0);
        scaleTransition.setFromY(0.0);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), initialScene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition, fadeOut);
        sequentialTransition.setOnFinished(event -> {
            try {
                showLoginWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sequentialTransition.play();
    }

    private void showLoginWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/login-signup-scene.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load());
        // Ensure that the controller is assigned
        LoginSignupController controller = fxmlLoader.getController();
        if (controller == null) {
            throw new IllegalStateException("Controller not found in FXML file.");
        }

        // Set reference to the main application if needed
        controller.setMainApp(this);

        // Optionally, validate scene content if there are specific requirements
        if (loginScene.getRoot() == null) {
            throw new IllegalStateException("FXML root layout might be invalid.");
        }


        // Transition Effects
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), loginScene.getRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Set the new scene on primary stage
        primaryStage.setScene(loginScene);
        primaryStage.setMaximized(true);
        fadeIn.play();
    }

    public void showDashboard() throws IOException {
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/com/tankwars/frontend/dashboard-main.fxml"));
        Scene dashboard = new Scene(dashboardLoader.load());
        dashboard.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        DashboardMainController controller = dashboardLoader.getController();
        controller.setMainApp(this);
        primaryStage.setScene(dashboard);
        primaryStage.setMaximized(true);
    }


}