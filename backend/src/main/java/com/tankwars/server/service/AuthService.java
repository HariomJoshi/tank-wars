package com.tankwars.server.service;


import com.tankwars.server.model.User;
import com.tankwars.server.repository.UserRepository;


import com.tankwars.server.utils.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Generate 6 digit OTP
    private  String generateOtp(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Send OTP to Email
    public void sendOtp(String email, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Tank-Wars One Time Password ");
        message.setText("Your OTP is "+otp);
        mailSender.send(message);
    }

    // Check if Username Exists
    public boolean usernameExists(String username){
        return userRepository.findByUsername(username) != null;
    }

    // Check if Email Exists
    public boolean emailExists(String email){
        return userRepository.findByEmail(email) != null;
    }

    // Register User with OTP verification
    public DTO register(User user){


        if(usernameExists(user.getUsername())){
            return new DTO( "Username already exists.", false);
        }

        if(emailExists(user.getEmail())){
            return new DTO("Email already exists.", false);
        }

        user.setOtp(generateOtp());
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encrypt password
        userRepository.save(user);
        sendOtp(user.getEmail(), user.getOtp());
        return new DTO("User registered. Please verify with OTP sent to your registered email.", false);
    }

    // Verify OTP
    public DTO verifyOtp(String email, String otp){
        User user = userRepository.findByEmail(email);
        if(user == null){
            return new DTO("Email not found", false);
        }

        if(!otp.equals(user.getOtp())){
            return new DTO("Invalid OTP", false);
        }

        user.setVerified(true);
        user.setOtp(null);
        userRepository.save(user);
        return new DTO("User verified successfully", true);
    }

    // Forgot Password (Generate Reset Token)
    public DTO forgotPassword(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            return new DTO("Email not found", false);
        }

        String resetToken = generateOtp();
        user.setResetToken(resetToken);
        userRepository.save(user);
        sendOtp(email, resetToken);
        return new DTO("Reset token sent to email", true);
    }


    // Reset Password using reset token
    public DTO resetPassword(String email, String resetToken, String newPassword){
        User user = userRepository.findByResetToken(resetToken);
        if(user == null || !user.getEmail().equals(email)){
            return new DTO("Invalid reset token.", false);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
        return new DTO("Password reset successfully.", true);
    }

    // find user by username

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}