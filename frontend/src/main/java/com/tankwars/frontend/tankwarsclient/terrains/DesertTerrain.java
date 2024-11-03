package com.tankwars.frontend.tankwarsclient.terrains;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class DesertTerrain extends Terrain{
    @Override
    public void drawTerrain(GraphicsContext gc) {
        int width = (int) gc.getCanvas().getWidth();
        int height = (int)  gc.getCanvas().getHeight();
        double[] dryMountainTerrain = generateTerrain(width, height);
        LinearGradient terrainGradient = new LinearGradient(0, 0, 0, 1, true, null,
                new Stop(0, Color.SANDYBROWN), new Stop(0.5, Color.SIENNA), new Stop(1, Color.SADDLEBROWN));
        gc.setFill(terrainGradient);
        gc.beginPath();
        gc.moveTo(0, height);

        for (int x = 0; x < width; x++) {
            gc.lineTo(x, dryMountainTerrain[x]);
        }
        gc.lineTo(width, height);
        gc.closePath();
        gc.fill();

        gc.setFill(Color.SANDYBROWN.deriveColor(0, 1, 1, 0.5));
        for(int i=0;i<width;i++){
            double y = dryMountainTerrain[i] + Math.random() * 10;
            gc.fillOval(i, y, 5+Math.random()*5,5+Math.random()*5);

        }
    }
}
