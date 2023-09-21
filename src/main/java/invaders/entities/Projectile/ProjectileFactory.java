package invaders.entities.Projectile;
import invaders.physics.Vector2D;

public class ProjectileFactory {

    public enum ProjectileType {
        FAST_STRAIGHT,
        SLOW_STRAIGHT
        // ... any other projectile types you might have
    }

    public static Projectile createProjectile(ProjectileType type, Vector2D startPosition) {
        switch (type) {
            case FAST_STRAIGHT:
                return new Projectile(new FastStraightBehavior(), startPosition);
            case SLOW_STRAIGHT:
                return new Projectile(new SlowStraightBehavior(), startPosition);
            // ... handle other projectile types as needed
            default:
                throw new IllegalArgumentException("Unknown projectile type: " + type);
        }
    }


}