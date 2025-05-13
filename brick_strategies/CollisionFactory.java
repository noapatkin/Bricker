package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * A factory class responsible for creating different collision strategies based on the specified depth.
 * The strategies are chosen randomly, with different probabilities for each type.
 * The class provides methods for creating collision strategies such as basic, puck, paddle, turbo, heart,
 * and more.
 */
 public class CollisionFactory {

    private final BrickerGameManager brickerGameManager;
    private final Random rand = new Random();

    /**
     * Constructs a new bricker.brick_strategies.CollisionFactory instance.
     *
     * @param brickerGameManager The game manager responsible for managing game objects.
     */
    public CollisionFactory(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Creates a random collision strategy based on the specified depth.
     * The depth determines the level of complexity of the collision strategy:
     * - depth 0: Can create basic strategies, puck, paddle, turbo, heart, or double collision.
     * - depth 1: Creates none basic strategy for the double strategy;
     * - depth 2: Like depth 2, but no double strategy here.
     *
     * @param depth The depth of the collision strategy, influencing the complexity and choice of strategy.
     * @return A randomly chosen collision strategy based on the given depth.
     */
    public CollisionStrategy createCollisionStrategy(int depth){
        CollisionStrategy collisionStrategy = null;
        if (depth == 0) {
            int collisionType = rand.nextInt(10);
            //0-4 is basic, 5 is puck, 6 is paddle, 7 is turbo, 8 is extra heart, 9 is double
            collisionStrategy = switch (collisionType) {
                case 0, 1, 2, 3, 4 -> new BasicCollisionStrategy(brickerGameManager);
                case 5 -> new PuckCollisionStrategy(brickerGameManager);
                case 6 -> new PaddleCollisionStrategy(brickerGameManager);
                case 7 -> new TurboCollisionStrategy(brickerGameManager);
                case 8 -> new HeartCollisionStrategy(brickerGameManager);
                case 9 -> new DoubleCollisionStrategy(brickerGameManager,
                        createCollisionStrategy(1), createCollisionStrategy(2));
                default -> collisionStrategy;
            };
            return collisionStrategy;
        }
        //got double in the first random
        else if (depth == 1) {
            int collisionType = rand.nextInt(5);
            collisionStrategy = switch (collisionType) {
                case 0 -> new PuckCollisionStrategy(brickerGameManager);
                case 1 -> new PaddleCollisionStrategy(brickerGameManager);
                case 2 -> new TurboCollisionStrategy(brickerGameManager);
                case 3 -> new HeartCollisionStrategy(brickerGameManager);
                case 4 -> new DoubleCollisionStrategy(brickerGameManager,
                        createCollisionStrategy(2), createCollisionStrategy(2));
                default -> collisionStrategy;
            };
            return collisionStrategy;
        }
        else{
            int collisionType = rand.nextInt(4);
            collisionStrategy = switch (collisionType) {
                case 0 -> new PuckCollisionStrategy(brickerGameManager);
                case 1 -> new PaddleCollisionStrategy(brickerGameManager);
                case 2 -> new TurboCollisionStrategy(brickerGameManager);
                case 3 -> new HeartCollisionStrategy(brickerGameManager);
                default -> collisionStrategy;
            };
            return collisionStrategy;
        }
    }
}
