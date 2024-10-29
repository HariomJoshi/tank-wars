package com.tankwars.server.controller;


import com.tankwars.server.model.User;
import com.tankwars.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController{

    @Autowired
    private AuthService authService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    // Register user and send OTP
    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody User user){
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username, email, and password are required.");
        }

        if(authService.usernameExists(user.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        if(authService.emailExists(user.getEmail())){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }

        authService.register(user);
        return ResponseEntity.ok("Please enter the OTP sent to your registered email.");
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
}
