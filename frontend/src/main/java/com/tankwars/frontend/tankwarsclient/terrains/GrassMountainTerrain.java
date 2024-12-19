package com.tankwars.frontend.tankwarsclient.terrains;

import com.tankwars.frontend.tankwarsclient.Tank;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class GrassMountainTerrain extends Terrain{

    private int width, height;
    private double[] mountainTerrain = generateTerrain(width, height);

    @Override
    public void drawTerrain(GraphicsContext gc) {
        width = (int) gc.getCanvas().getWidth();
        height = (int) gc.getCanvas().getHeight();

        LinearGradient terrainGradient = new LinearGradient(0, 0, 0, 1, true, null,
                new Stop(0, Color.DARKGREEN), new Stop(1, Color.FORESTGREEN));
        gc.setFill(terrainGradient);
        // Initialize mountainTerrain based on the width and height
        if (mountainTerrain == null || mountainTerrain.length != width) {
            mountainTerrain = generateTerrain(width, height);
        }
        gc.beginPath();
        gc.moveTo(0, height);

        for (int x = 0; x < width; x++) {
            gc.lineTo(x, mountainTerrain[x]);
        }
        gc.lineTo(width, height);
        gc.closePath();
        gc.fill();

        gc.setFill(Color.FORESTGREEN.deriveColor(0, 1, 1, 0.5));
        for(int i=0;i<width;i++){
            double y = mountainTerrain[i] + Math.random() * 10;
            gc.fillOval(i, y, 5+Math.random()*5,5+Math.random()*5);
        }
    }

    @Override
    public double getHeightAt(int posX) {
//        // Normalize posX to fit within the bounds of the array
//        if (mountainTerrain.length > 0) {
//            int normalizedX = (int) ((posX / width) * mountainTerrain.length);
//
//            // Clamp the value to ensure it's within valid bounds
//            normalizedX = Math.max(0, Math.min(normalizedX, mountainTerrain.length - 1));
//
//            return mountainTerrain[normalizedX];
//        }
//
//        // Return a default height if mountainTerrain is not properly initialized
//        return height;
        return mountainTerrain[posX];
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
        return mountainTerrain;
    }

}
