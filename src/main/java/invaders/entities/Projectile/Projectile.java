package invaders.entities.Projectile;

import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;
import java.io.File;

public class Projectile implements Moveable, Renderable {
    private final ProjectileBehavior behavior;
    private final Vector2D position;
    private Image image;
    private final double width = 10;  // Adjust as needed
    private final double height = 20; // Adjust as needed


    public Projectile(ProjectileBehavior behavior, Vector2D startPosition) {
        this.behavior = behavior;
        this.position = startPosition;
        this.image = new Image(new File("src/main/resources/projectile.png").toURI().toString(), width, height, true, true);
    }

    public void move() {
        behavior.move(this);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void turnBlack() {
        this.image = new Image(new File("src/main/resources/black_projectile.png").toURI().toString(), width, height, true, true);
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(String imagePath) {
        this.image = new Image(new File(imagePath).toURI().toString());
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }

    @Override
    public void left() {

    }

    @Override
    public void right() {

    }
}