package com.tankwars.frontend.controllers;

import com.tankwars.frontend.tankwarsclient.Animations;
import com.tankwars.frontend.tankwarsclient.InitializeGame;
import com.tankwars.frontend.utils.ApiClient;
import com.tankwars.frontend.utils.User;
import com.tankwars.frontend.utils.Valid;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
    private Button resetLink;

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
    private Button loginButton;

    @FXML
    private Button buttonExitGame;

    private User currentUser = User.getInstance();

    @FXML
    public void initialize() {
        registerLink.setOnAction(event -> {
            Animations.flipTransition(loginPane, signupPane);
        });

        resetLink.setOnAction(event->{
            Animations.flipTransition(loginPane, resetPasswordPane);
        });

        loginLink.setOnAction(event -> {
            Animations.flipTransition(signupPane, loginPane);
        });

        buttonGenerateOTP.setOnAction(e->{
            handleSendOtp();
        });

        buttonChangePassword.setOnAction(e->{
            handleResetPassword();
        });

        buttonRegisterUser.setOnAction(e->{
            handleRegisterUser();
        });

        loginButton.setOnAction(e->{
            handleLoginUser();
        });

        buttonExitGame.setOnAction(e->{
            handleExitGame();
        });

    }

    private void handleSendOtp(){
//        flipTransition(loginPane, resetPasswordPane);
//        System.out.println("Here");
            if(boxResetPasswordEmail.getText().isEmpty()){
                Alert emptyBoxAlert = new Alert(Alert.AlertType.ERROR);
                emptyBoxAlert.setTitle("Error");
                emptyBoxAlert.setHeaderText("Error in reset password");
                emptyBoxAlert.setContentText("Please enter a valid email address");
                emptyBoxAlert.show();
            }else{
                System.out.println(boxResetPasswordEmail.getText());
                String params = String.format("email=%s", boxResetPasswordEmail.getText());
                String fullUrl = "http://localhost:8080/api/auth/forgot-password?"+params;
                ApiClient client = new ApiClient();
                client.sendPostReqQuery(fullUrl).thenAccept(res->{
                    Platform.runLater(()->{
                        if(Boolean.TRUE.equals(res)){
                            buttonGenerateOTP.setVisible(false);
                            boxResetPasswordEmail.setEditable(false);
                            resetPasswordPane.setPrefWidth(551);
                            resetPasswordPane.setPrefHeight(727);
                            labelEnterOTP.setVisible(true);
                            labelNewPassword.setVisible(true);
                            boxEnterOTP.setVisible(true);
                            boxNewPassword.setVisible(true);
                            buttonChangePassword.setVisible(true);
                            System.out.println("OTP sent successfully");

                        }else{
                            System.out.println("Not able to send email");

                        }
                    });
                }).exceptionally(err->{
                    System.out.println("some error occured while sending mail");
                    err.printStackTrace();
                    return null;
                });
            }
    }

    private void handleResetPassword(){
        System.out.println("here");
        if(boxEnterOTP.getText().isEmpty() || boxNewPassword.getText().isEmpty()){
            Alert emptyBoxAlert = new Alert(Alert.AlertType.ERROR);
            emptyBoxAlert.setTitle("Error");
            emptyBoxAlert.setHeaderText("Error in reset password");
            emptyBoxAlert.setContentText("OTP/new password section missing");
            emptyBoxAlert.show();
        }else{
            String params = String.format("email=%s&resetToken=%s&newPassword=%s", boxResetPasswordEmail.getText(), boxEnterOTP.getText(), boxNewPassword.getText());
            String fullUrl = "http://localhost:8080/api/auth/reset-password?"+params;
            ApiClient client = new ApiClient();
            client.sendPostReqQuery(fullUrl).thenAccept(res->{
                Platform.runLater(()->{
                    if(Boolean.TRUE.equals(res)){
                        System.out.println("Password Reset successfully");
                        buttonGenerateOTP.setVisible(true);
                        boxResetPasswordEmail.setEditable(true);
                        labelEnterOTP.setVisible(false);
                        labelNewPassword.setVisible(false);
                        boxEnterOTP.setVisible(false);
                        boxNewPassword.setVisible(false);
                        buttonChangePassword.setVisible(false);
                        Animations.flipTransition(resetPasswordPane, loginPane);
                    }else{
                        System.out.println("Not able to reset password");
                    }
                });
            }).exceptionally(err->{
                System.out.println("Some error occured while changing password");
                err.printStackTrace();
                return null;
            });
        }
    }



    @FXML
    private TextField loginUsername;
    @FXML
    private TextField loginPassword;

    // button that handles logging in user
    ApiClient client = new ApiClient();
    Valid valid = new Valid();



    private void handleLoginUser() throws RuntimeException{
        HashMap<String, String> mp = new HashMap<>();
        mp.put("username", loginUsername.getText());
        mp.put("password", loginPassword.getText());
        client.sendPostRequest("http://localhost:8080/api/auth/login", mp).thenAccept(res->{
           Platform.runLater(()->{
                if(Boolean.TRUE.equals(res)){
                       // TODO: animation to the dashboard
                    try {
                        currentUser.setUsername(loginUsername.getText());
                        mainApp.showDashboard();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Logged in successful");
               }else{
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setContentText("Username or password wrong");
                   alert.show();
                   System.out.println("Username or password wrong");
               }
           });
        }).exceptionally(err->{
            System.out.println("Some error occured while logging user in");
            err.printStackTrace();
            return null;
        });
    }

    // fields related to register user
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

        if(!valid.emailValid(email.getText())){
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setContentText("Please enter a valid email");
            alert.show();
            return;
        }
//         here all the details are valid, so we need to register the user

        HashMap<String, String> mp = new HashMap<>();
        mp.put("username", username.getText());
        mp.put("password", password.getText());
        mp.put("email", email.getText());
        client.sendPostRequest("http://localhost:8080/api/auth/signup", mp).thenAccept(res-> {
            Platform.runLater(()->{
                if (Boolean.FALSE.equals(res)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Some error occured while sending data, please try again");
                    alert.show();
                    System.out.println("Rejected");
                    return;
                } else {
                    System.out.println("Accepted");
                    Animations.flipTransition(signupPane, verifyOTPPane);
                    buttonVerifyOTP.setOnAction(e->{
                        verifyEmail(email.getText(), boxVerifyOTP.getText()).thenAccept(result->{
                            Platform.runLater(()->{
                                if(Boolean.FALSE.equals(result)){
                                    System.out.println("OTP verification failed");
                                }else{
                                    System.out.println("verification successful");
                                    Animations.flipTransition(verifyOTPPane, loginPane);
                                }
                            });
                        });
                    });
                }
            });
        }).exceptionally(err->{
            System.out.println("Some error occured is signup");
            err.printStackTrace();
            return null;
        });
    }

    CompletableFuture<Boolean> verifyEmail(String email, String otp){
        // we need to declare a hashmap in order to send emails
        String queryParams = String.format("email=%s&otp=%s", email, otp);
        String fullUrl = "http://localhost:8080/api/auth/verify" + "?" + queryParams;
        // query is complete
        return client.sendPostReqQuery(fullUrl).thenApply(res->{
            if(Boolean.FALSE.equals(res)){
                System.out.println("Invalid OTP");
                return false;
            }else{
                System.out.println("verified successfully");
                return true;
            }

        }).exceptionally(err->{
            System.out.println("Some error occured in verify email");
            err.printStackTrace();
            return false;
        });
    }

    // function to exit the game
    private void handleExitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Confirm exit");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    // reference of the previous class, since it has been initialized,
    // we cannot initialize it again, we need the reference of that class only
    private InitializeGame mainApp;
    public void setMainApp(InitializeGame gameWindow) {
        mainApp = gameWindow;
    }




}
