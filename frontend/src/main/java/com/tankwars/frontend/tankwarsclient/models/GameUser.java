package com.tankwars.frontend.tankwarsclient.models;

import com.tankwars.frontend.tankwarsclient.Tank;

public class GameUser {
    private int health;
    private Tank tank;

    private String name;

    public GameUser(String name, Tank tank){
        this.name = name;
        this.health = 100;
        this.tank = tank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void decreaseHealth(int amount){
        health -= amount;
    }

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

}
