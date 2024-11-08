package com.tankwars.server.service;

import com.tankwars.server.model.User;
import com.tankwars.server.repository.UserRepository;
import com.tankwars.server.utils.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    // Method to send a friend request
    public DTO sendFriendRequest(String currentUsername, String targetUsername){
        User currentUser = userRepository.findByUsername(currentUsername);
        User targetUser = userRepository.findByUsername(targetUsername);
        if(targetUser == null){
            return new DTO("Target user not found.",false);
        }
        if(currentUser == null){
            return new DTO("Current user not found.", false);
        }
        if(targetUser.getPendingFriends().contains(currentUsername)){
            return new DTO("Request already send.", false);
        }
        if(currentUser.getAvailableFriends().contains(targetUsername)){
            return new DTO("Already friends.",false);
        }
        targetUser.getPendingFriends().add(currentUsername);
        userRepository.save(targetUser);
        return new DTO("Friend request sent successfully.",true);
    }

    //Method to get pending friend requests
    public List<String> getPendingRequests(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            return new ArrayList<>();
        }
        return  user.getPendingFriends();
    }

    //Method to get all friends
    public List<String> getFriends(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            return new ArrayList<>();
        }
        return user.getAvailableFriends();
    }

    // Method to accept friend request
    public DTO acceptFriendRequest(String currentUsername, String requesterUsername){
        User currentUser = userRepository.findByUsername(currentUsername);
        User requester = userRepository.findByUsername(requesterUsername);

        if(currentUser == null || requester ==null){
            return new DTO("User not found.", false);
        }

        if(!currentUser.getPendingFriends().contains(requesterUsername)){
            return new DTO("Request not found.",false);
        }

        currentUser.getPendingFriends().remove(requesterUsername);
        currentUser.getAvailableFriends().add(requesterUsername);
        requester.getAvailableFriends().add(currentUsername);

        userRepository.save(currentUser);
        userRepository.save(requester);

        return new DTO("Friend request accepted.",true);
    }
}
