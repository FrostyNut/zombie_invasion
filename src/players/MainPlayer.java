package players;

import gamemanager.Input;
import gamemanager.RuntimeSettings;
import gamemanager.Settings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


/**
 * The Main Player will be controlled by the user.
 *
 * @author Jackie Chan
 * Apr 17, 2016
 */
public class MainPlayer extends Player {    
    
    
    /** The input listener for the main player. */
    private final Input input;
    
    
    private int health = 50;
    
    /*
        Constructs a new Player object.
    */
    public MainPlayer(Pane pane, Image img, double x, double y, double r, double velX, double velY, double velR, Input input) {
        super(pane, img, x, y, r, velX, velY, velR);
        this.input = input;
        this.imageView = new ImageView(Settings.getMainPlayerImage());
        this.imageView.relocate(x, y);
    }

    
    /**
     * Sets velocities on the x and y axis.
     */
    public void changeValues() {
        if(input.isMoveDown() && y < RuntimeSettings.getMaxPlayerSpawnY()) {
            velY = Settings.SPEED;
        } else if(input.isMoveUp() && y > 0) {
            velY = -Settings.SPEED;
        } else {
            velY = 0;
        }
        
        if(input.isMoveLeft() && x > 0) {
            velX = -Settings.SPEED;
        } else if(input.isMoveRight() && x < RuntimeSettings.getMaxPlayerSpawnX()) {
            velX = Settings.SPEED;
        } else {
            velX = 0;
        }
        
        r = input.getAngle();
    }        
    
    public void deductHealth(int amount) {
        health -= amount;
    }
    
    public int getHealth() {
        return health;
    }
    
}
