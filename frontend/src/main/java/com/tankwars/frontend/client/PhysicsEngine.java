package com.tankwars.frontend.client;

import javafx.application.Platform;

public class PhysicsEngine implements Runnable {
    private double power;
    private double angle;
    private String weapon;
    private double weight;
    private GameUIController controller;

    public void setParameters(double power, double angle, String weapon, double weight) {
        this.power = power;
        this.angle = angle;
        this.weapon = weapon;
        this.weight = weight;
    }

    @Override
    public void run() {
        // Simulate projectile trajectory based on parameters
        double x = 0, y = 0;
        double velocityX = power * Math.cos(Math.toRadians(angle));
        double velocityY = power * Math.sin(Math.toRadians(angle));

        while (y >= 0) {
            x += velocityX * 0.1;
            y += velocityY * 0.1 - 0.5 * 9.8 * Math.pow(0.1, 2);
            velocityY -= 9.8 * 0.1;

            double finalX = x;
            double finalY = y;

            Platform.runLater(() -> controller.updateUIWithProjectile(finalX, finalY));
            try {
                Thread.sleep(50); // Delay to simulate projectile movement
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
