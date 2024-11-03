package com.tankwars.frontend.utils;

public class User {
    private static final User instance = new User();

    private User(){}  // private contructor to prevent instantiation

    public static User getInstance() {
        return instance;
    }

    // following are the properties of user, and since we will only have on user so we need a singleton class here
    private String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
