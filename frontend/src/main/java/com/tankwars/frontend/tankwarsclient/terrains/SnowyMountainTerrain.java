package com.tankwars.frontend.tankwarsclient.terrains;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class SnowyMountainTerrain extends Terrain{
    @Override
    public void drawTerrain(GraphicsContext gc) {
        int width = (int) gc.getCanvas().getWidth();
        int height = (int) gc.getCanvas().getHeight();
        double[] snowyTerrain = generateTerrain(width, height);

        // Gradient for a snowy terrain
        LinearGradient terrainGradient = new LinearGradient(0, 0, 0, 1, true, null,
                new Stop(0, Color.WHITE), new Stop(0.5, Color.LIGHTGRAY), new Stop(1, Color.DARKGRAY));
        gc.setFill(terrainGradient);

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
}
