package com.tankwars.frontend.tankwarsclient;

public abstract class Weapon{
    private String name;
    private int damage;
    private String type;
    private int weight;
    private double posX, posY;

    public Weapon(String name, int damage, String type, int weight, double posX, double posY){
        this.name = name;
        this.damage = damage;
        this.type = type;
        this.weight = weight;
        this.posX = posX;
        this.posY = posY;
    }

    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.type;
    }

    public int getDamage(){
        return this.damage;
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
