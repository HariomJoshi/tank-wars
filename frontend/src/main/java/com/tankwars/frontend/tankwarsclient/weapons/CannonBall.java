package com.tankwars.frontend.tankwarsclient.weapons;

import javafx.scene.image.Image;
import com.tankwars.frontend.tankwarsclient.Animations;
import javafx.stage.Stage;

public class CannonBall extends Weapon{
    private Image icon;
    private final int damage = 25;
    private final int radius = 5;

    public CannonBall(String name, String type, int weight) {
        super(name, type, weight);
        icon = new Image(getClass().getResource("/images/cannon_ball.png").toExternalForm());
    }

    @Override
    public String toString() {
        return super.getName(); // Only return the name for display in the ComboBox
    }

    public Image getIcon(){
        return this.icon;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public void playAnimation(Stage source, double x, double y) {
        Animations.cannonBallEffect(x, y, source.getScene());
    }
}
