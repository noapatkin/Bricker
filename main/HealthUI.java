package bricker.main;

import bricker.gameobjects.Heart;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Manages the health UI in the Bricker game.
 * The class handles the display of the player's health, which is represented by hearts.
 * It also manages the creation, removal, and updating of health-related elements on the UI.
 */
 public class HealthUI {
    private static final int STARTING_HEARTS = 3;
    private static final int MAX_HEARTS = 4;
    private static final int HEARTS_DIM = 20;
    private final BrickerGameManager brickerGameManager;
    private final ImageReader imageReader;
    private final Heart[] health;
    private TextRenderable healthRender;
    private int cur_health;
    public static boolean needToAddHeart = false;

    /**
     * Constructs a new bricker.main.HealthUI instance.
     *
     * @param brickerGameManager The game manager responsible for managing game objects.
     */
    HealthUI(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
        imageReader = brickerGameManager.getImageReader();
        this.health = new Heart[MAX_HEARTS];
    }

    /**
     * Creates a new heart object with an image and the appropriate size.
     *
     * @return A new bricker.gameobjects.Heart instance.
     */
    private Heart createHeart(){
        Renderable heartImage = imageReader.readImage("assets/heart.png", false);
        return new Heart(Vector2.ZERO, new Vector2(HEARTS_DIM, HEARTS_DIM), heartImage);
    }

    /**
     * Creates the health counter displayed on the UI.
     */
    private void createCounter(){
        this.healthRender = new TextRenderable(Integer.toString(STARTING_HEARTS));
        GameObject livesCount = new GameObject(new Vector2(0,
                brickerGameManager.getWindowDimensions().y()-HEARTS_DIM),
                new Vector2(HEARTS_DIM, HEARTS_DIM), healthRender);
        brickerGameManager.addObject(livesCount, Layer.UI);
    }

    /**
     * Adds a heart to the UI to represent an increase in health.
     * Does nothing if the maximum number of hearts has been reached.
     */
    public void addHeart(){
        if (cur_health==MAX_HEARTS){
            return;
        }
        health[cur_health] = createHeart();
        health[cur_health].setTopLeftCorner(new Vector2(HEARTS_DIM*(cur_health+1),
                brickerGameManager.getWindowDimensions().y()-HEARTS_DIM));
        brickerGameManager.addObject(health[cur_health++], Layer.UI);
    }

    /**
     * Initializes the health UI with the starting number of hearts.
     * Also creates the health counter text object.
     */
    public void startHealth(){
        for(int i=0; i<STARTING_HEARTS; i++){
            addHeart();
        }
        createCounter();
    }

    /**
     * Removes one heart from the UI, representing the loss of a life.
     */
    public void removeHeart(){
        if(cur_health > 0){
            brickerGameManager.removeObject(health[--cur_health], Layer.UI);
        }
    }

    /**
     * Updates the health UI based on the current health state.
     * Adds a heart if necessary, updates the health text, and removes hearts that have collided.
     */
    public void updateHealth(){
        if (needToAddHeart && cur_health < MAX_HEARTS){
            addHeart();
            needToAddHeart = false;
        }
        //the needToAddHeart turns true if health is full so when
        // you lose a life it doesn't change so this is my dumb solution
        if (cur_health == MAX_HEARTS){
            needToAddHeart = false;
        }
        updateText();
        brickerGameManager.removeObject(Heart.getCollisionHeartDelete());
        Heart.setCollisionHeart();
    }

    /**
     * Updates the health counter text, changing its color based on the current health.
     */
    private void updateText(){
        healthRender.setString(Integer.toString(cur_health));
        if(cur_health >= 3){
            healthRender.setColor(Color.GREEN);
        } else if (cur_health == 2) {
            healthRender.setColor(Color.YELLOW);
        }else if (cur_health == 1) {
            healthRender.setColor(Color.RED);
        }
    }

    /**
     * Adds a collision heart to the game that falls down from a specified position.
     *
     * @param center    The position where the heart will appear.
     * @param fallSpeed The speed at which the heart will fall.
     */
    public void addCollisionHeart(Vector2 center, int fallSpeed){
        Heart collisionHeart = createHeart();
        collisionHeart.setCenter(center);
        collisionHeart.setVelocity(Vector2.DOWN.mult(fallSpeed));
        brickerGameManager.addObject(collisionHeart);
    }

    /**
     * Gets the current health (number of hearts remaining).
     *
     * @return The current health.
     */
    public int getCurHealth(){return cur_health;}
}
