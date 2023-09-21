package invaders.entities.Projectile;

// FastStraightBehavior class
public class FastStraightBehavior implements ProjectileBehavior {
    private static final int SPEED = 3; // Adjust as needed

    @Override
    public void move(Projectile projectile) {
        projectile.getPosition().setY(projectile.getPosition().getY() + SPEED);
    }
}
