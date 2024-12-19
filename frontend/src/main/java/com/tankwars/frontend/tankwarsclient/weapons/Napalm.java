package com.tankwars.frontend.tankwarsclient.weapons;

import com.tankwars.frontend.tankwarsclient.Animations;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Napalm extends Weapon{

    private Image icon;
    private final int damage = 18;
    private final int radius = 10;

    public Napalm(String name, String type, int weight) {
        super(name, type, weight);
        icon = new Image(Objects.requireNonNull(getClass().getResource("/images/icon_napalm.png")).toExternalForm());
    }

    @Override
    public String toString() {
        return super.getName(); // Only return the name for display in the ComboBox
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public void playAnimation(Stage source, double x, double y) {
        Animations.napalmStrikeEffect(x, y, source.getScene());
    }

    public Image getIcon(){
        return this.icon;
    }
}
