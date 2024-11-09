package com.tankwars.server.utils;

import java.util.concurrent.ConcurrentHashMap;

// class in singleton, since only one instance is required for this class
public class UserRegistry {
    private static UserRegistry instance;
    private final ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>(); // username to session ID
    private final ConcurrentHashMap<String, String> sessionUsers = new ConcurrentHashMap<>(); // session ID to username

    private void UserRegistery(){};

    public static UserRegistry getInstance(){
        if(instance == null) instance = new UserRegistry();
        return instance;
    }

    public void register(String username, String sessionId) {
        userSessions.put(username, sessionId);
        sessionUsers.put(sessionId, username);
    }

    public void unregister(String username) {
        String sessionId = userSessions.remove(username);
        sessionUsers.remove(sessionId);
    }

    public String getSessionIdByUsername(String username) {
        return userSessions.get(username);
    }

    public String getUsernameBySessionId(String sessionId) {
        return sessionUsers.get(sessionId);
    }

    public boolean isUserOnline(String username) {
        return sessionUsers.containsKey(username);
    }
}

