package com.tankwars.frontend.models;

import com.tankwars.frontend.helper.TType;

public class GameState {

    // Single instance of GameState
    private static GameState instance;

    private TType terrain;
    private int player1Health;
    private int player2Health;
    private String roomId;

    // Private constructor to prevent instantiation
    private GameState() {
    }

    // Public method to provide access to the single instance
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public TType getTerrain() {
        return terrain;
    }

    public void setTerrain(TType terrain) {
        this.terrain = terrain;
    }

    public int getPlayer1Health() {
        return player1Health;
    }

    public void setPlayer1Health(int player1Health) {
        this.player1Health = player1Health;
    }

    public int getPlayer2Health() {
        return player2Health;
    }

    public void setPlayer2Health(int player2Health) {
        this.player2Health = player2Health;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
