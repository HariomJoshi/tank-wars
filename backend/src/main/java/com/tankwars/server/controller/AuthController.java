package com.tankwars.server.controller;


import com.tankwars.server.model.User;
import com.tankwars.server.service.AuthService;
import com.tankwars.server.utils.DTO;
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
    public ResponseEntity<DTO> verifyOtp(@RequestParam String email, @RequestParam String otp) {

        DTO result = authService.verifyOtp(email, otp);
        if(!result.success) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return ResponseEntity.ok(result);
    }

    //Forgot Password (Send Reset Token)
    @PostMapping("/forgot-password")
    public ResponseEntity<DTO> forgotPassword(@RequestParam String email){
        DTO result = authService.forgotPassword(email);
        if(!result.success){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    // Reset Password using Resent Token
    @PostMapping("/reset-password")
    public ResponseEntity<DTO> resetPassword(@RequestParam String email, @RequestParam String resetToken, @RequestParam String newPassword){
        DTO result = authService.resetPassword(email, resetToken, newPassword);
        if(!result.success){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return ResponseEntity.ok(result);
    }

    // Login with Username and Password
    @PostMapping("/login")
    public ResponseEntity<DTO> login(@RequestBody User user) {
        User foundUser = authService.findByUsername(user.getUsername());

        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            if (!foundUser.isVerified()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DTO("Account not verified. Please verify via OTP.", false));
            }
            return ResponseEntity.ok(new DTO("Login successful!", true));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DTO("Invalid username or password!", false));
    }
}