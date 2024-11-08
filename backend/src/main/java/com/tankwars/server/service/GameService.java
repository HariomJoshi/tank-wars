package com.tankwars.server.service;

import com.tankwars.server.model.GameState;
import com.tankwars.server.model.Player;
import com.tankwars.server.model.Weapon;

public class GameService {
    private GameState gameState;

    public GameService() {
        this.gameState = new GameState();
    }

    public void processPlayerAction(Player player, Weapon weapon, int angle, int power) {
        // Broadcast action to both players (with weight of weapon)
    }

    public void updateHealth(Player player, int damage) {
        // Update player's health based on collision results
        if(player == gameState.getPlayer1()){
            gameState.getPlayer1().reduceHealth(damage);
        }else{
            gameState.getPlayer2().reduceHealth(damage);
        }
    }

    public GameState getGameState() {
        return gameState;
    }
}
