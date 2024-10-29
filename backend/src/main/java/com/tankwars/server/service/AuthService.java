package com.tankwars.server.service;



import com.tankwars.server.model.User;
import com.tankwars.server.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Pattern;

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
    public String register(User user){


        if(usernameExists(user.getUsername())){
            return "Username already exists.";
        }

        if(emailExists(user.getEmail())){
            return "Email already exists.";
        }

        user.setOtp(generateOtp());
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encrypt password
        userRepository.save(user);
        sendOtp(user.getEmail(), user.getOtp());
        return "User registered. Please verify with OTP sent to your registered email.";
    }

    // Verify OTP
    public String verifyOtp(String email, String otp){
        User user = userRepository.findByEmail(email);
        if(user == null){
            return "Email not found.";
        }

        if(!otp.equals(user.getOtp())){
            return "Invalid OTP.";
        }

        user.setVerified(true);
        user.setOtp(null);
        userRepository.save(user);
        return "User verified successfully.";
    }

    // Forgot Password (Generate Reset Token)
    public String forgotPassword(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            return "Email not found";
        }

        String resetToken = generateOtp();
        user.setResetToken(resetToken);
        userRepository.save(user);
        sendOtp(email, resetToken);
        return "Reset token sent to email";
    }


    // Reset Password using reset token
    public String resetPassword(String email, String resetToken, String newPassword){
        User user = userRepository.findByResetToken(resetToken);
        if(user == null || !user.getEmail().equals(email)){
            return "Invalid reset token.";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
        return "Password reset successfully.";
    }

    // find user by username

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
