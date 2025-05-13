package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that applies two different collision strategies when a collision occurs.
 * When a collision happens, both specified strategies are executed in sequence.
 */
 public class DoubleCollisionStrategy extends BasicCollisionStrategy {

    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    /**
     * Constructs a new DoubleCollision instance.
     *
     * @param brickerGameManager The game manager responsible for managing game objects.
     * @param strategy1          The first collision strategy to apply.
     * @param strategy2          The second collision strategy to apply.
     */
    public DoubleCollisionStrategy(BrickerGameManager brickerGameManager, CollisionStrategy strategy1,
                                   CollisionStrategy strategy2) {
        super(brickerGameManager);
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    /**
     * Defines the behavior when a collision occurs by applying both collision strategies.
     *
     * @param object1 The first object involved in the collision.
     * @param object2 The second object involved in the collision.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        super.onCollision(object1, object2);
        strategy1.onCollision(object1, object2);
        strategy2.onCollision(object1, object2);
    }
}
