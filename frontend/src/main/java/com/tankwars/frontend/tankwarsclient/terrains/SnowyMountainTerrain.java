package com.tankwars.frontend.tankwarsclient.terrains;

import com.tankwars.frontend.tankwarsclient.Tank;
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
    public double getHeightAt(int posX) {
//        // Ensure the position is within the bounds of the array
//        if (posX < 0 || posX >= snowyTerrain.length) {
//            // Return a default value (e.g., the bottom of the canvas) if out of bounds
//            return height;
//        }

        // Cast posX to int as array indices must be integers
        return snowyTerrain[posX];
    }

    // Method to place an object in section 2
    @Override
    public double[] placeObjectInSection(int section,GraphicsContext gc, Tank tank) {
        int width = (int) gc.getCanvas().getWidth();
        int perSectionWidth = width / 15;  // Based on 15 sections
        int startX = section * perSectionWidth;
        int endX = (section + 1) * perSectionWidth;

        // Find a random x within this section (e.g., randomly select a position in section 2)
        int randomX = startX + (int) (Math.random() * (endX - startX));

        // Get the height at the chosen x coordinate
        double heightAtX = getHeightAt(randomX);

        // Draw the tank at this position
//        tank.draw(gc, randomX, heightAtX, tank);  // Draw the tank at the correct position
        return new double[]{randomX, heightAtX};
    }

    public double[] getTerrainHeights(){
        return snowyTerrain;
    }

}