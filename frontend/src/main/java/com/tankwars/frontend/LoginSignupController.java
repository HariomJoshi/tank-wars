package com.tankwars.frontend;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;

import java.util.regex.Pattern;

public class LoginSignupController {


    // attributes for OTP verification
    @FXML
    private Pane verifyOTPPane;
    @FXML
    private TextField boxVerifyOTP;
    @FXML
    private Button buttonVerifyOTP;
    @FXML
    private Button buttonRegisterUser;

    // attributes for reset password
    @FXML
    private Pane resetPasswordPane;
    @FXML
    private TextField boxResetPasswordEmail;
    @FXML
    private Label labelEnterOTP;
    @FXML
    private TextField boxEnterOTP;
    @FXML
    private Label labelNewPassword;
    @FXML
    private TextField boxNewPassword;
    @FXML
    private Button buttonChangePassword;
    @FXML
    private Button buttonGenerateOTP;
    @FXML
    private Button resetPasswordButton;

    // attributes for login/signup switching
    @FXML
    private Pane loginPane;
    @FXML
    private Pane signupPane;
    @FXML
    private Button registerLink;
    @FXML
    private Button loginLink;

    @FXML
    public void initialize() {
        registerLink.setOnAction(event -> {
            flipTransition(loginPane, signupPane);
        });

        loginLink.setOnAction(event -> {
            flipTransition(signupPane, loginPane);
        });

        resetPasswordButton.setOnAction(e->{
            handleResetPassword();
        });

        buttonRegisterUser.setOnAction(e->{
            handleRegisterUser();
        });
    }

    private void flipTransition(Node currentPane, Node nextPane) {
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

    private void handleResetPassword(){
        flipTransition(loginPane, resetPasswordPane);
        buttonGenerateOTP.setOnAction(e->{
            if(boxResetPasswordEmail.getText().isEmpty()){
                Alert emptyBoxAlert = new Alert(Alert.AlertType.ERROR);
                emptyBoxAlert.setTitle("Error");
                emptyBoxAlert.setHeaderText("Error in reset password");
                emptyBoxAlert.setContentText("Please enter a valid email address");
                emptyBoxAlert.show();
            }else{
                buttonGenerateOTP.setVisible(false);
                boxResetPasswordEmail.setEditable(false);
                resetPasswordPane.setPrefWidth(551);
                resetPasswordPane.setPrefHeight(727);
                labelEnterOTP.setVisible(true);
                labelNewPassword.setVisible(true);
                boxEnterOTP.setVisible(true);
                boxNewPassword.setVisible(true);
                buttonChangePassword.setVisible(true);
            }
        });
    }

    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;

    private void handleRegisterUser() {
        if(!password.getText().equals(confirmPassword.getText())){
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setContentText("password is not equal to confirm password");
            alert.show();
            return;
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}$");
        String emailStr  = email.getText();
        if(!pattern.matcher(emailStr).matches()){
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setContentText("Please enter a valid email");
            alert.show();
            return;
        }

        flipTransition(signupPane, verifyOTPPane);
        buttonVerifyOTP.setOnAction(e->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("OTP verification working");
            alert.show();
        });
    }
}
