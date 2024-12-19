package com.tankwars.frontend.tankwarsclient.models;

public class TwoPlayers {
    private TwoPlayers(){};
    private static TwoPlayers instance;
    public static boolean isTwoPlayerGame = false;

    public static TwoPlayers getInstance(){
        if(instance == null){
            instance = new TwoPlayers();
        }
        return instance;
    }

    public String getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(String firstUser) {
        isTwoPlayerGame = true;
        this.firstUser = firstUser;
    }

    public String getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(String secondUser) {
        isTwoPlayerGame = true;
        this.secondUser = secondUser;
    }

    public void setInstance(TwoPlayers instance) {
        this.instance = instance;
    }

    private String firstUser, secondUser;
}
