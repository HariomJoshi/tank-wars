package com.tankwars.server.service;


import com.tankwars.server.model.User;
import com.tankwars.server.repository.UserRepository;


import com.tankwars.server.utils.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;




    // Generate  6 digit OTP
    private String generateOtp(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    //Send OTP to Email
    public void sendOtp(String email, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Tank-Wars One Time Password ");
        message.setText("Your OTP is "+otp);
        mailSender.send(message);
    }


    // check if Username Exists
    public boolean usernameExists(String username){
        return userRepository.findByUsername(username) != null;
    }

    // Register USer with OTP verification
    public String register(User user){

        if (usernameExists(user.getUsername())) {
            return "Username already exists.";
        }
        if (emailExists(user.getEmail())) {
            return "Email already exists.";
        }

        user.setOtp(generateOtp());
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
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
        return "User verified successfully";

    }

    // Check if Email Exists
    public boolean emailExists(String email){
        return userRepository.findByEmail(email) != null;
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

    // Method to send a friend request
    public String sendFriendRequest(String currentUsername, String targetUsername){
        User currentUser = userRepository.findByUsername(currentUsername);
        User targetUser = userRepository.findByUsername(targetUsername);
        if(targetUser == null){
            return "Target user not found!";
        }
        if(currentUser == null){
            return "Current user not found!";
        }
        if(targetUser.getPendingFriends().contains(currentUsername)){
            return "Request already sent.";
        }
        if(currentUser.getAvailableFriends().contains(targetUsername)){
            return "Already friends.";
        }
        targetUser.getPendingFriends().add(currentUsername);
        userRepository.save(targetUser);
        return "Friend request sent successfully";
    }

    // Method to get pending friend requests
    public List<String> getPendingRequests(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            return new ArrayList<>();
        }
        return user.getPendingFriends();
    }

    // Method to get all friends
    public List<String> getFriends(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            return new ArrayList<>();
        }
        return user.getAvailableFriends();
    }

    // Method to accept friend request
    public String acceptFriendRequest(String currentUsername, String requesterUsername){
        User currentUser = userRepository.findByUsername(currentUsername);
        User requester = userRepository.findByUsername(requesterUsername);
        if(currentUser == null || requester == null){
            return "User not found!";
        }

        if(!currentUser.getPendingFriends().contains(requesterUsername)){
            return "Request not found.";
        }

        currentUser.getPendingFriends().remove(requesterUsername);
        currentUser.getAvailableFriends().add(requesterUsername);
        requester.getAvailableFriends().add(currentUsername);

        userRepository.save(currentUser);
        userRepository.save(requester);

        return "Friend request accepted.";
    }
}
