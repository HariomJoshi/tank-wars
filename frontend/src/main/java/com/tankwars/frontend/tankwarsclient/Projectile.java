package com.tankwars.frontend.tankwarsclient;

import javafx.geometry.Point2D;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Projectile {
    private final double angle; // in degrees
    private final double power; // initial speed
    private final double weight; // weight of the weapon
    private final double gravity; // constant
    private final double initialX;
    private final double initialY;
    private final int width, height;

    public Projectile(double angle, double power, double weight, double initialX, double initialY, int width, int height) {
        this.angle = angle;
        this.power = power;
        this.weight = weight;
        this.gravity = 9.81;
        this.initialX = initialX;
        this.initialY = initialY;
        this.width = width;
        this.height = height;
    }



    public List<Point2D> calculateTrajectoryPoints(Projectile projectile) {
        System.out.println("Projectile "+initialX+" "+initialY);
        List<Point2D> points = new ArrayList<>();
        double angleRadians = Math.toRadians(angle);
        double adjustedPower = power / Math.sqrt(1 + weight / 10.0);
        double gravity = 9.81;
        double timeOfFlight = (2 * adjustedPower * Math.sin(angleRadians)) / gravity;




        for(double t =0 ; t <= 25; t += 0.1) {
            double x = initialX + (adjustedPower * Math.cos(angleRadians) * t);
            double y = initialY + (adjustedPower * Math.sin(angleRadians) * t - 0.5 * gravity * t * t);


            System.out.println("TOF:"+timeOfFlight);

            // Check if the projectile is out of the screen bounds
            if(x < 0 || x > width || y < 0 || y > height) {

                System.out.println("Projectile is out of screen bounds.");
                break;  //Stop if projectile goes out of bound
            }
            points.add(new Point2D(x,y));
        }
        return points;
    }

    public double getAngle() {
        return angle;
    }

    public double getPower() {
        return power;
    }

    public double getWeight() {
        return weight;
    }

    public double getInitialX() {
        return  initialX;
    }

    public double getInitialY() {
        return  initialY;
    }
}