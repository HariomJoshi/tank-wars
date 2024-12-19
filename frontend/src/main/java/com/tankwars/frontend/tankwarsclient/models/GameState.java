package com.tankwars.frontend.tankwarsclient.models;

import com.tankwars.frontend.tankwarsclient.Tank;

public class GameState {
    private GameUser player1;

    public GameUser getPlayer2() {
        return player2;
    }

    public GameUser getPlayer1() {
        return player1;
    }

    private GameUser player2;


    private GameUser currentTurn;

    private static GameState state;
    private GameState(){};
    // making the class singleton
    public static GameState getInstance(){
        if(state== null){
            state = new GameState();
        }
        return state;
    }

    public GameUser getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(GameUser currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setPlayer1(GameUser user){
        player1 = user;
    }

    public void setPlayer2(GameUser user){
        player2 = user;
    }
    private boolean flag = true;
    public void switchTurns(){
        if(flag){
            setCurrentTurn(player2);
        }else{
            setCurrentTurn(player1);
        }
        flag = !flag;
    }


}
