package com.tankwars.server.controller;


import com.tankwars.server.service.UserService;
import com.tankwars.server.utils.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    // Send friend request
    @PostMapping("/sendRequest")
    public ResponseEntity<DTO> sendFriendRequest(@RequestParam String currentUsername, @RequestParam String targetUsername) {
        DTO result = userService.sendFriendRequest(currentUsername, targetUsername);
        return switch (result.message) {
            case "Target user not found." -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            case "Current user not found." -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            case "Request already sent." -> ResponseEntity.status(HttpStatus.CONFLICT).body(result);
            case "Already friends." -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            default ->
                // Success status
                    ResponseEntity.ok(result);
        };
    }

    // Get pending friend requests
    @GetMapping("/getRequests")
    public ResponseEntity<List<String>> getFriendRequests(@RequestParam String username) {
        List<String> pendingRequests = userService.getPendingRequests(username);
        if (pendingRequests == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(pendingRequests);
    }

    // Fetch all friends
    @GetMapping("/myFriends")
    public ResponseEntity<List<String>> getFriends(@RequestParam String username){
        List<String> friends = userService.getFriends(username);
        if(friends == null ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return  ResponseEntity.ok(friends);
    }

    // Accept friend request
    @PostMapping("/acceptRequest")
    public ResponseEntity<DTO> acceptFriendRequest(@RequestParam String currentUsername, @RequestParam String requesterUsername){
        DTO result = userService.acceptFriendRequest(currentUsername, requesterUsername);
        if(!result.success){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

}