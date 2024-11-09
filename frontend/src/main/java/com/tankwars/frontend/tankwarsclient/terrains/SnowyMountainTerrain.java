package com.tankwars.frontend.tankwarsclient.terrains;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class SnowyMountainTerrain extends Terrain{

    int width, height;
    private double[] snowyTerrain = generateTerrain(width, height);

    @Override
    public void drawTerrain(GraphicsContext gc) {
        width = (int) gc.getCanvas().getWidth();
        height = (int) gc.getCanvas().getHeight();


        // Gradient for a snowy terrain
        LinearGradient terrainGradient = new LinearGradient(0, 0, 0, 1, true, null,
                new Stop(0, Color.WHITE), new Stop(0.5, Color.LIGHTGRAY), new Stop(1, Color.DARKGRAY));
        gc.setFill(terrainGradient);

        // Initialize mountainTerrain based on the width and height
        if (snowyTerrain == null || snowyTerrain.length != width) {
            snowyTerrain = generateTerrain(width, height);
        }

        // Draw the terrain
        gc.beginPath();
        gc.moveTo(0, height);
        for (int x = 0; x < width; x++) {
            gc.lineTo(x, snowyTerrain[x]);
        }
        gc.lineTo(width, height);
        gc.closePath();
        gc.fill();

        // Optional: Draw snow texture using semi-transparent circles
        gc.setFill(Color.LIGHTBLUE.deriveColor(0, 1, 1, 0.3)); // Light snow texture
        for (int x = 0; x < width; x += 10) {
            double y = snowyTerrain[x] + Math.random() * 10; // Slightly above the terrain
            gc.fillOval(x, y, 5 + Math.random() * 5, 5 + Math.random() * 5); // Random sizes
        }
    }

    @Override
    public double getHeightAt(double posX) {
//        // Ensure the position is within the bounds of the array
//        if (posX < 0 || posX >= snowyTerrain.length) {
//            // Return a default value (e.g., the bottom of the canvas) if out of bounds
//            return height;
//        }

        // Cast posX to int as array indices must be integers
        return snowyTerrain[(int) posX];
    }

}
