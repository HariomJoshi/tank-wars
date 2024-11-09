package com.tankwars.frontend.tankwarsclient;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Tank extends Group {

    private Rectangle body;
    private Rectangle turretBase;
    private Rectangle gunBarrel;
    private Rectangle tracks;
    private Circle[] wheels;
    private boolean isFacingLeft;
    private Rotate gunRotation; // Rotate transformation for the barrel
    private double height = 45;

    public Tank(double x, double y, Color bodyColor, Color trackColor, boolean isFacingLeft) {
        this.isFacingLeft = isFacingLeft;

        // Tank Body
        body = new Rectangle(60, 30);
        body.setFill(bodyColor);
        body.setArcWidth(10);
        body.setArcHeight(10);
        body.setLayoutX(0);
        body.setLayoutY(0);

        // Tank Tracks
        tracks = new Rectangle(65, 15, trackColor);
        tracks.setArcWidth(5);
        tracks.setArcHeight(5);
        tracks.setLayoutX(-2.5);
        tracks.setLayoutY(20);

        // Tank Wheels
        wheels = new Circle[5];
        for (int i = 0; i < wheels.length; i++) {
            wheels[i] = new Circle(5, Color.DARKGRAY);
            wheels[i].setCenterX(10 + i * 12);
            wheels[i].setCenterY(28);
        }

        // Turret Base
        turretBase = new Rectangle(30, 15, Color.DARKGRAY);
        turretBase.setArcWidth(5);
        turretBase.setArcHeight(5);
        turretBase.setLayoutX(15);
        turretBase.setLayoutY(-5);

        // Gun Barrel
        gunBarrel = new Rectangle(20, 4, Color.DARKGRAY);
        gunBarrel.setLayoutX(45);  // Default position when facing right
        gunBarrel.setLayoutY(-3);

        // Add rotation for the barrel
        gunRotation = new Rotate();
        gunRotation.setPivotX(0);   // Set rotation pivot to start of barrel
        gunRotation.setPivotY(2);   // Center vertically within barrel
        gunBarrel.getTransforms().add(gunRotation);

        // Add all parts to the Tank group
        getChildren().addAll(tracks, body, turretBase, gunBarrel);
        getChildren().addAll(wheels);

        // Position tank initially
        setLayoutX(x);
        setLayoutY(y);

        // Apply initial facing direction
        setFacingDirection(isFacingLeft);
        gunRotation.setPivotX(0);   // Set pivot to the start of the barrel for rotation
        gunRotation.setPivotY(gunBarrel.getHeight() / 2.0); // Center vertically for correct rotation behavior
    }

    public void setFacingDirection(boolean isFacingLeft) {
        this.isFacingLeft = isFacingLeft;

        if (isFacingLeft) {
            turretBase.setScaleX(-1);
            gunBarrel.setScaleX(-1);
            turretBase.setLayoutX(15);
            gunBarrel.setLayoutX(5);
            gunRotation.setAngle(0); // Reset angle if flipping direction
        } else {
            turretBase.setScaleX(1);
            gunBarrel.setScaleX(1);
            turretBase.setLayoutX(15);
            gunBarrel.setLayoutX(45);
            gunRotation.setAngle(0); // Reset angle if flipping direction
        }
    }

    // Method to update barrel angle
    public void setBarrelAngle(double angle) {
        // Clamp the angle between 1 and 90 degrees
        angle = Math.max(1, Math.min(angle, 90));

        if (isFacingLeft) {
            gunRotation.setAngle(-angle); // Invert angle for left-facing tanks
        } else {
            gunRotation.setAngle(-angle);
        }
    }

    public double getHeight() {
        return height;
    }

    public void updatePosition(double x, double terrainHeight) {
        setLayoutX(x);
        setLayoutY(terrainHeight - body.getHeight() - tracks.getHeight());
    }
}
