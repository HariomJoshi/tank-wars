package com.tankwars.frontend.tankwarsclient.weapons;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class Weapon{
    private String name;
    private String type;
    private int weight;
    private double posX, posY;

    public abstract int getDamage();
    public abstract void playAnimation(Stage source, double x, double y);
    public abstract Image getIcon();

    public Weapon(String name, String type, int weight){
        this.name = name;
        this.type = type;
        this.weight = weight;
    }

    public void setPosX(double posX){
        this.posX = posX;
    }

    public void setPosY(double posY){
        this.posY = posY;
    }

    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.type;
    }

    public int getWeight(){
        return this.weight;
    }

    public double getPosY(){
        return posY;
    }

    public double getPosX() {
        return posX;
    }
}
