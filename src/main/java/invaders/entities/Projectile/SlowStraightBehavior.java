package invaders.entities.Projectile;

// SlowStraightBehavior class
public class SlowStraightBehavior implements ProjectileBehavior {
    private static final int SPEED = 1; // Adjust as needed

    @Override
    public void move(Projectile projectile) {
        projectile.getPosition().setY(projectile.getPosition().getY() + SPEED);
    }
}
