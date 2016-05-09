package gamemanager;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * These are the default settings for the game.
 *
 * @author Jackie Chan
 * Apr 17, 2016
 */
public class Settings {

    
    /*
    NOTE:
        The settings dealing with player start location and window size will not
        be final in the future. These settings will need to be changed during 
        gameplay if the user is to spawn in a different place, or the map size
        is different.
    */
    
    /** The speed at which the player will move. */
    public static final int SPEED   = 3;              
    
    
    /**
     * The player's width. This is based off the size of the player's image.
     */
    public static final int PLAYER_WIDTH = 30;
    
    
    /**
     * The player's height. This is based off the size of the player's image.
     */
    public static final int PLAYER_HEIGHT = 30;
    
    
    /**
     * The explosion's width. This is based off the size of the explosion gif.
     */
    public static final int EXPLOSION_WIDTH = 30;
    
    
    /**
     * The explosion's height. This is based off the size of the explosion gif.
     */
    public static final int EXPLOSION_HEIGHT = 30;
 
    
    /** The default background color for the game play scene. */
    public static final Color BACKGROUND = Color.WHITE;                        
    
    /**
     * This is the gif displayed when something explodes.
     */
    public static final Image EXPLOSION = new Image("/explosion.gif", true);
    
    
    /**
     * This is the image that is displayed on the bullet.
     */
    public static final Image BULLET = new Image("/bullet.png", true);        
    
    
    /**
     * The amount a bullet can move at once would be large if going from (0,0)
     * to (100,100). The calculateSlope method in GameMath takes care of this by 
     * scaling the rise of 100 and run of 100 to a value below 3. How was this 
     * value chosen? At random.
     */
    public static final double SCALE_MAX = 20.0;
    
    
    /**
     * The delay between each movement of the bullet. The bullet moves using an
     * animation timer, so that makes it difficult to check times, or even use
     * Thread.sleep(). So when the handle method sends a time in nanoseconds,
     * we record that time. Then it sends another time since the events are
     * still being fired. If the difference between those two times is less than
     * or equal to three (Settings.BULLET_MOVEMENT_DELAY), then move the bullet.
     * If it is not, then don't do anything.
     */
    public static final int BULLET_MOVEMENT_DELAY = 3;
    
    
    /**
     * The firing rate.
     */
    public static final int BULLET_FIRING_DELAY = 100;
    
    
    /**
     * The amount of time the explosion image can be shown.
     */
    public static final int EXPLOSION_DURATION = 300;
    
    
    public static final int REFRESH_RATE = 45;
    
    
    public static Image getMainPlayerImage() {
        return new Image("/main_player_icon.png",true);
    }
    
    public static Image getEnemyImage(int type) {
        return new Image("/enemy"+type+".gif", true);
    }
    
    public static Image getDeadPlayerImage() {
        return new Image("/dead_player.png", true);
    }
}
