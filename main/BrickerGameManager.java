package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The main class for managing the Bricker game.
 * This class is responsible for initializing the game, managing game objects,
 * and handling game state transitions.
 * It extends the `GameManager` class from the `danogl` library.
 *
 * @author [Your Name or Team Name]
 */
public class BrickerGameManager extends GameManager {

    //Constants
    //general
    private static final int DEF_ROWS = 7;
    private static final int DEF_COLS = 8;
    private static final int WALL_SIZE = 15;

    //ball
    private static final float BALL_SPEED = 300;
    private static final int BALL_DIM = 20;
    private final float TURBO_FACTOR = 1.4f;
    private final int MAX_TURBO = 6;

    //brick
    private static final int BRICK_HEIGHT = 15;

    //paddle
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;

    //hearts
    private HealthUI healthUI;

    //Class fields
    //general
    private final int rows;
    private final int cols;
    private final Vector2 windowDimensions;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private WindowController windowController;

    //for objects tracking
    private Ball ball;
    private TextRenderable healthRender;

    //strategies
    private ExtraPaddle extraPaddle;
    public static boolean isTurbo = false;

    //images (and sound)
    private Renderable ballImage;
    private Renderable paddleImage;
    private Renderable puckImage;
    private Renderable backgroundImage;
    private Renderable brickImage;
    private Renderable turboImage;
    private Sound collisionSound;

    //Constructors
    /**
     * Constructor for the bricker.main.BrickerGameManager class.
     * Initializes the game with default row and column counts.
     *
     * @param windowTitle       The title of the game window.
     * @param windowDimensions  The dimensions of the game window.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.rows = DEF_ROWS;
        this.cols = DEF_COLS;
        this.windowDimensions = windowDimensions;

    }

    /**
     * Constructor for the bricker.main.BrickerGameManager class.
     * Initializes the game with specified row and column counts.
     *
     * @param windowTitle       The title of the game window.
     * @param windowDimensions  The dimensions of the game window.
     * @param rows              The number of rows in the game layout.
     * @param cols              The number of columns in the game layout.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int rows, int cols) {
        super(windowTitle, windowDimensions);
        this.rows = rows;
        this.cols = cols;
        this.windowDimensions = windowDimensions;
    }

    //Methods
    /**
     * Initializes the game. Sets up the game window, objects, and initial state.
     *
     * @param imageReader       Utility for reading and managing game images.
     * @param soundReader       Utility for reading and managing game sounds.
     * @param inputListener     Listener for user inputs.
     * @param windowController  Controller for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        //initialising the hearts here helps when restarting the game
        this.extraPaddle = null;
        this.healthUI = new HealthUI(this);

        //preventing turbo bugs
        isTurbo = false;

        //saving images of objects
        this.ballImage = imageReader.readImage("assets/ball.png", true);
        this.paddleImage = imageReader.readImage("assets/paddle.png", true);
        this.puckImage = imageReader.readImage("assets/mockBall.png", true);
        this.backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        this.brickImage = imageReader.readImage("assets/brick.png", true);
        this.turboImage = imageReader.readImage("assets/redball.png", true);
        this.collisionSound = soundReader.readSound("assets/blop.wav");

        //Creating starting objects
        createBackground();
        createWalls();
        createBall();
        createPaddle();
        createBrickWall();
        healthUI.startHealth();
    }

    /**
     * Updates the game state. Handles collision strategies, checks win/lose conditions,
     * and updates health UI.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        //Update healthUI
        healthUI.updateHealth();

        //Check if ball in bounds and if not create a new one
        ballOutBounds();
        //Check if game won
        if (isWin()){
            if(windowController.openYesNoDialog("You Won! Play Again?")){
                windowController.resetGame();
            }
            else{
                windowController.closeWindow();
            }
        }
        //Delete anything that is out of bounds
        int cols = ball.getCollisionCounter();
        removeAllOutOfBounds();
        ball.setCollusionCounter(cols);
        //Handling Collision strategies
        if(extraPaddle != null){
            if(extraPaddle.getHits() == 4){
                gameObjects().removeGameObject(extraPaddle);
                extraPaddle = null;
            }
        }
        if(ball.getCollisionCounter() == MAX_TURBO){
                removeTurbo();
        }
    }

    /**
     * Creates the game background using the specified image asset.
     */
    //Objects creations
    private void createBackground(){
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Creates the walls that define the boundaries of the game area.
     */
    private void createWalls(){
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WALL_SIZE,
                windowDimensions.y()), null);
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x()-WALL_SIZE, 0),
                new Vector2(WALL_SIZE, windowDimensions.y()), null);
        GameObject upWall = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), WALL_SIZE), null);
        gameObjects().addGameObject(leftWall);
        gameObjects().addGameObject(rightWall);
        gameObjects().addGameObject(upWall);
    }

    /**
     * Creates the ball object, setting its initial position, velocity, and appearance.
     */
    private void createBall(){
        Ball ball = new Ball(Vector2.ZERO, new Vector2(BALL_DIM, BALL_DIM),
                ballImage, collisionSound);
        ball.setCenter(windowDimensions.mult(0.5F));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()){
            ballVelX *= -1;
        }
        if(rand.nextBoolean()){
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setTag("ball");
        this.ball = ball;
        gameObjects().addGameObject(ball);
    }

    /**
     * Creates the paddle object, setting its dimensions, appearance, and initial position.
     */
    private void createPaddle(){
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH,PADDLE_HEIGHT), paddleImage,
                inputListener, windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, (float) (windowDimensions.y()*0.9)));
        paddle.setTag("paddle");
        gameObjects().addGameObject(paddle);
    }

    /**
     * Creates a wall of bricks based on the number of rows and columns specified.
     */
    private void createBrickWall(){
        CollisionFactory collisionFactory= new CollisionFactory(this);
        float brickWidth = (windowDimensions.x()-(WALL_SIZE*2))/cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float colC = WALL_SIZE+(j*brickWidth);
                float rowC = WALL_SIZE+(i*BRICK_HEIGHT);
                Brick brick = new Brick(new Vector2(colC, rowC), new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage, collisionFactory.createCollisionStrategy(0));
                brick.setTag("brick");
                gameObjects().addGameObject(brick);
            }
        }
    }

    /**
     * Checks if the player has lost the game.
     *
     * @return true if the player's health has reached zero, false otherwise.
     */
    //Game finishers
    private boolean isLose(){
        return healthUI.getCurHealth() == 0;
    }

    /**
     * Checks if the player has won the game.
     *
     * @return true if all bricks are destroyed or a win condition is met, false otherwise.
     */
    private boolean isWin(){
        if(inputListener.isKeyPressed(KeyEvent.VK_W)){
            return true;
        }
        int brickNum = 0;
        for (GameObject gameObject : gameObjects()) {
            if (gameObject.getTag().equals("brick")) {
                brickNum++;
            }
        }
        return brickNum == 0;
    }

    /**
     * Checks if a specific game object is out of bounds.
     *
     * @param gameObject The game object to check.
     * @return true if the object is out of bounds, false otherwise.
     */
    //Bounds handling
    private boolean outOfBounds(GameObject gameObject){
        float objectX = gameObject.getCenter().x();
        float objectY = gameObject.getCenter().y();
        return objectX > windowDimensions.x() || objectX < 0 || objectY > windowDimensions.y() || objectY < 0;
    }

    /**
     * Removes all game objects that are out of bounds.
     */
    private void removeAllOutOfBounds(){
        for (GameObject object : gameObjects()){
            if(outOfBounds(object)){
                gameObjects().removeGameObject(object);
            }
        }
    }

    /**
     * Removes the specified game object from the game.
     *
     * @param object The game object to remove.
     * @return true if the object was removed, false otherwise.
     */
    //Methods for strategies
    public boolean removeObject(GameObject object){
        return gameObjects().removeGameObject(object);
    }

    /**
     * Removes the specified game object from the game in a specific layer.
     *
     * @param object The game object to remove.
     * @param Layer  The layer from which to remove the object.
     * @return true if the object was removed, false otherwise.
     */
    public boolean removeObject(GameObject object, int Layer){
        return gameObjects().removeGameObject(object, Layer);
    }

    /**
     * Adds a game object to the game.
     *
     * @param object The game object to add.
     */
    public void addObject(GameObject object){
        gameObjects().addGameObject(object);
    }

    /**
     * Adds a game object to the game in a specific layer.
     *
     * @param object The game object to add.
     * @param Layer  The layer in which to add the object.
     */
    public void addObject(GameObject object, int Layer){
        gameObjects().addGameObject(object, Layer);
    }

    /**
     * Creates and returns a puck object. The puck is a smaller ball with specific behavior.
     *
     * @return The created puck object.
     */
    public Ball createPuck(){
        Ball puck = new Ball(Vector2.ZERO, Vector2.ONES.mult(BALL_DIM*0.75f),
                puckImage, collisionSound);
        Random rand = new Random();
        double angle = rand.nextDouble() * Math.PI;
        float velX = (float)Math.cos(angle) * BALL_SPEED;
        float velY = (float)Math.sin(angle) * BALL_SPEED;
        puck.setVelocity(new Vector2(velX, velY));
        puck.setTag("puck");
        return puck;
    }

    //extra paddle
    /**
     * Checks if there is currently an extra paddle in the game.
     *
     * @return true if no extra paddle exists, false otherwise.
     */
    public boolean hasExtraPaddle(){
        return extraPaddle == null;
    }

    /**
     * Creates and returns an extra paddle object. The extra paddle appears in the middle of the screen.
     *
     * @return The created extra paddle object.
     */
    public ExtraPaddle createExtraPaddle(){
        ExtraPaddle extraPaddle = new ExtraPaddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH,PADDLE_HEIGHT),
                paddleImage, inputListener, windowDimensions);
        extraPaddle.setCenter(new Vector2(windowDimensions.x()/2, (windowDimensions.y()/2)));
        this.extraPaddle = extraPaddle;
        return extraPaddle;
    }

    //turbo
    /**
     * Sets the game into turbo mode by increasing the ball's speed and changing its appearance.
     */
    public void setTurbo(){
        this.ball.renderer().setRenderable(turboImage);
        this.ball.setVelocity(ball.getVelocity().mult(TURBO_FACTOR));
        isTurbo = true;
    }

    /**
     * Removes turbo mode, resetting the ball's speed and appearance to normal.
     */
    private void removeTurbo(){
        this.ball.renderer().setRenderable(ballImage);
        this.ball.setVelocity(ball.getVelocity().mult(1/TURBO_FACTOR));
        ball.setCollusionCounter(0);
        isTurbo = false;
    }

    /**
     * checking if ball is out of bounds, in case it is remove a heart, check if game is lost
     * and create new ball if needed
     */
    private void ballOutBounds(){
        float ballHeight = ball.getCenter().y();
        if(ballHeight > windowDimensions.y()) {
            healthUI.removeHeart();
            if (isLose()){
                if(windowController.openYesNoDialog("You Lost! Play Again?")){
                    windowController.resetGame();
                }
                else{
                    windowController.closeWindow();
                }
            }
            createBall();
            //checking if ball was turbo
            if(isTurbo){
                setTurbo();
            }
        }
    }

    /**
     * @return The image reader used in the game.
     */
    public ImageReader getImageReader(){return imageReader;}

    /**
     * @return The dimensions of the game window.
     */
    public Vector2 getWindowDimensions(){return windowDimensions;}

    /**
     * Adds a heart object that will collide with other game objects.
     *
     * @param center    The center position of the heart.
     * @param fallSpeed The speed at which the heart falls.
     */
    public void addCollisionHeart(Vector2 center, int fallSpeed){
        healthUI.addCollisionHeart(center, fallSpeed);
    }

    /**
     * Main entry point for the Bricker game. Launches the game window.
     *
     * @param args If there is, used for number of rows and cols for the brick wall.
     */
    public static void main(String[] args) {
        GameManager window;
        if (args.length == 2){
            window = new BrickerGameManager("Bricker", new Vector2(500, 650),
                    Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }
        else {
            window = new BrickerGameManager("Bricker", new Vector2(500, 650));
        }
        window.run();
    }
}
