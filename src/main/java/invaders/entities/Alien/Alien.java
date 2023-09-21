package invaders.entities.Alien;

import invaders.engine.GameEngine;
import invaders.entities.Projectile.Projectile;
import invaders.entities.Projectile.ProjectileFactory;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import javafx.scene.image.Image;

import java.io.File;

public class Alien implements Renderable {
    private final Vector2D position;
    private Image image;
    private final double width = 25; // Adjust as needed
    private final double height = 30; // Adjust as needed
    private static double HORIZONTAL_SPEED = 0.5; // Adjust as needed
    private static final int VERTICAL_STEP = 5;  // Adjust as needed

    private GameEngine gameEngine;

    public Alien(Vector2D position, String imageFilename, GameEngine gameEngine) {
        this.position = position;
        this.image = new Image(new File(imageFilename).toURI().toString(), width, height, true, true);
        this.gameEngine = gameEngine;
    }

    public void increaseSpeed(double increment) {
        HORIZONTAL_SPEED += increment;
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    public void setImage(String imagePath) {
        this.image = new Image(new File(imagePath).toURI().toString(), width, height, true, true);
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

    public void left() {
        this.position.setX(this.position.getX() - HORIZONTAL_SPEED);
    }

    public void right() {
        this.position.setX(this.position.getX() + HORIZONTAL_SPEED);
    }

    public void descend() {
        this.position.setY(this.position.getY() + VERTICAL_STEP);
    }

    public void shoot() {
        // Decide the type of projectile based on a random value
        ProjectileFactory.ProjectileType type = Math.random() > 0.5 ?
                ProjectileFactory.ProjectileType.FAST_STRAIGHT :
                ProjectileFactory.ProjectileType.SLOW_STRAIGHT;

        // Calculate the starting position for the projectile
        Vector2D startPosition = new Vector2D(
                this.getPosition().getX() + this.getWidth() / 2 - 4, // Center the projectile horizontally
                this.getPosition().getY() + this.getHeight() // Position it below the alien
        );

        // Create the projectile using the factory
        Projectile projectile = ProjectileFactory.createProjectile(type, startPosition);

        // Add the projectile to the game world
        gameEngine.addProjectile(projectile);
    }

}
