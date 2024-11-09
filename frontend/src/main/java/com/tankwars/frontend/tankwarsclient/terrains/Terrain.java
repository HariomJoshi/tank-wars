package com.tankwars.frontend.tankwarsclient.terrains;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.Random;

public abstract class Terrain {
    public abstract void drawTerrain(GraphicsContext gc);

    protected double[] generateTerrain(int width, int height){
        double[] points = new double[width];
        int sections = 10;
        double baseHeight = height * 0.5;
        double maxPeakHeight = height * 0.3;
        double[] peaks = new double[sections+1];
        for(int i=0;i<=sections;i++)
            peaks[i] = baseHeight + (Math.random() - 0.5) * maxPeakHeight;

        int perSectionWidth = width / sections;
        for(int i=0;i<sections;i++){
            int startX = i*perSectionWidth;
            int endX = (i+1) * perSectionWidth;
            double startHeight = peaks[i];
            double endHeight = peaks[i+1];
            for(int x=startX;x<endX;x++){
                double ratio = (double) (x - startX) / perSectionWidth;
                double midHeight = (1 - ratio) * startHeight + ratio * endHeight;
                // Add slight random fluctuations for natural appearance
                points[x] = midHeight + (Math.random() - 0.5) * 5; // Minimal variability for smoothness
            }
        }
        return points;
    }

    public void drawSky(GraphicsContext gc) {
        Random random = new Random();
        int skyType = random.nextInt(4); // 0 = day, 1 = night, 2 = evening, 3 = cloudy

        switch (skyType) {
            case 0 -> drawDaySky(gc);
            case 1 -> drawNightSky(gc);
            case 2 -> drawEveningSky(gc);
            case 3 -> drawCloudySky(gc);
        }
    }

    private void drawDaySky(GraphicsContext gc) {
        int width = (int) gc.getCanvas().getWidth();
        int height = (int) gc.getCanvas().getHeight();
        RadialGradient daySky = new RadialGradient(0, 0, width / 2, height / 2, height / 2,
                false, null, new Stop(0, Color.LIGHTSKYBLUE), new Stop(1, Color.DEEPSKYBLUE));
        gc.setFill(daySky);
        gc.fillRect(0, 0, width, height);
    }

    private void drawNightSky(GraphicsContext gc) {
        int width = (int) gc.getCanvas().getWidth();
        int height = (int) gc.getCanvas().getHeight();
        RadialGradient nightSky = new RadialGradient(0, 0, width / 2, height / 2, height / 2,
                false, null, new Stop(0, Color.MIDNIGHTBLUE), new Stop(1, Color.BLACK));
        gc.setFill(nightSky);
        gc.fillRect(0, 0, width, height);
    }

    private void drawEveningSky(GraphicsContext gc) {
        int width = (int) gc.getCanvas().getWidth();
        int height = (int) gc.getCanvas().getHeight();
        LinearGradient eveningSky = new LinearGradient(0, 0, 0, 1, true, null,
                new Stop(0, Color.ORANGE), new Stop(1, Color.DARKRED));
        gc.setFill(eveningSky);
        gc.fillRect(0, 0, width, height);
    }

    private void drawCloudySky(GraphicsContext gc) {
        int width = (int) gc.getCanvas().getWidth();
        int height = (int) gc.getCanvas().getHeight();

        // Draw gradient sky
        LinearGradient cloudySky = new LinearGradient(0, 0, 0, 1, true, null,
                new Stop(0, Color.LIGHTGRAY), new Stop(1, Color.DARKGRAY));
        gc.setFill(cloudySky);
        gc.fillRect(0, 0, width, height);

        // Add clouds
        gc.setFill(Color.WHITE);
        gc.setGlobalAlpha(0.5); // Make clouds partially transparent
        for (int i = 0; i < 10; i++) {
            double x = Math.random() * width;
            double y = Math.random() * (height / 2); // Clouds only in the upper half
            double cloudWidth = 100 + Math.random() * 100; // Random width
            double cloudHeight = 50 + Math.random() * 30;  // Random height
            gc.fillOval(x, y, cloudWidth, cloudHeight);
        }
        gc.setGlobalAlpha(1.0); // Reset opacity for other drawings
    }
}
