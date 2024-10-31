package com.tankwars.server.controller;


import com.tankwars.server.model.User;
import com.tankwars.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    // Register user and send OTP
    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username, email, and password are required.");
        }

        if (authService.usernameExists(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        if (authService.emailExists(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists!");
        }

        authService.register(user);
        return ResponseEntity.ok("Please enter the OTP sent to your registered email!");
    }

    //Verify OTP
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {

        String resultMessage = authService.verifyOtp(email, otp);
        if(resultMessage.contains("Invalid") || resultMessage.contains("not found")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMessage);
        }
        return ResponseEntity.ok(resultMessage);
    }



    //Forgot Password (Send Reset Token)
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email){
        String resultMessage = authService.forgotPassword(email);
        if(resultMessage.equals("Email not found")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMessage);
        }
        return ResponseEntity.ok(resultMessage);
    }

    // Reset Password using Resent Token
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String resetToken, @RequestParam String newPassword){
        String resultMessage = authService.resetPassword(email, resetToken, newPassword);
        if(resultMessage.equals("Invalid reset token")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMessage);
        }
        return ResponseEntity.ok(resultMessage);
    }


    // Login with Username and Password
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User foundUser = authService.findByUsername(user.getUsername());
        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            if (!foundUser.isVerified()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not verified. Please verify via OTP.");
            }
            return ResponseEntity.ok("Login successful!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
    }


    // Send Friend Request
    @PostMapping("/sendRequest")
    public ResponseEntity<String> sendFriendRequest(@RequestParam String currentUsername, @RequestParam String targetUsername){
        String result = authService.sendFriendRequest(currentUsername, targetUsername);
        return switch (result) {
            case "User not found!" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            case "Request already sent." ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body("Friend request already sent.");
            case "Already friends." -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are already friend.");
            default -> ResponseEntity.ok("Friend request sent successfully.");
        };
    }


    // Get pending friend requests
    @GetMapping("/getRequests")
    public ResponseEntity<List<String>> getFriendRequests(@RequestParam String username){
        List<String> pendingRequests = authService.getPendingRequests(username);
        if(pendingRequests == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(pendingRequests);
    }


    // Fetch all friends
    @GetMapping("/myFriends")
    public ResponseEntity<List<String>> getFriends(@RequestParam String username){
        List<String> friends = authService.getFriends(username);
        if(friends == null ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return  ResponseEntity.ok(friends);
    }


    // Accept friend request
    @PostMapping("/acceptRequest")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam String currentUsername, @RequestParam String requesterUsername){
        String result = authService.acceptFriendRequest(currentUsername, requesterUsername);
        if(result.equals("Request not found")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend request not found.");
        }
        return ResponseEntity.ok("Friend request accepted.");
    }

}
