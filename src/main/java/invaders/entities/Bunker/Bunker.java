package invaders.entities.Bunker;

import invaders.logic.Damagable;
import invaders.rendering.Renderable;
import invaders.physics.Vector2D;
import javafx.scene.image.Image;
import java.io.File;

public class Bunker implements Damagable, Renderable {
    private final Vector2D position;
    private BunkerState state;
    private double health = 3; // 3 hits before destruction

    private final double width = 50; // Adjust as per your image size
    private final double height = 25; // Adjust as per your image size

    public Bunker(Vector2D position) {
        this.position = position;
        this.state = new GreenState(); // Initial state
    }

    @Override
    public void takeDamage(double amount) {
        this.health -= amount;
        state.handleHit(this);
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public Image getImage() {
        return new Image(new File("src/main/resources/" + state.getImageFilename()).toURI().toString(), width, height, true, true);
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    public void setState(BunkerState state) {
        this.state = state;
    }
}
