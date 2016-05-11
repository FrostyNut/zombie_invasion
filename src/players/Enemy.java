package players;

import gamemanager.GameManager;
import gamemanager.GameMath;
import gamemanager.Settings;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * This is the Enemy class. When shot, the bullet that collided with the enemy
 * will call the deductHealth() method to reduct this enemy's health.
 *
 * @author Jackie Chan
 * Apr 25, 2016
 */
public class Enemy extends Player {
    
    // The health of this enemy. 
    private int currentHealth = 5;
    
    // The previous time this enemy attacked.
    private long previousTime = 0;
    private int scaledDistance = 0;
    private int damage = 0;    
    
   
    /**
     * Constructor for the Enemy class. Creates a new instance of the Enemy class. 
     * 
     * @see players.Player
     * 
     * @param speed     The speed of this enemy. 
     * @param damage    The damage this enemy causes. 
     */
    public Enemy(Pane pane, 
                    Image img, 
                    double x, 
                    double y, 
                    double r, 
                    double velX,
                    double velY,
                    double velR, 
                    int speed,
                    int damage) {
        
        super(pane, img, x, y, r, velX, velY, velR);        
        this.scaledDistance = speed;
        this.damage = damage;
    }

    
    /**
     * Moves this enemy and then attacks the main player if possible. 
     * @param now 
     */
    public void attackPlayer(long now) {
        
        long time = now / 1000000;

        if(previousTime == 0) previousTime = time;
        if(imageView.getBoundsInParent().intersects(GameManager.mainPlayer.getImageView().getBoundsInParent())
                && time - previousTime >= 500) {
            GameManager.mainPlayer.deductHealth(damage);
            System.out.println("Enemy:\tAttacking Main Player");
            previousTime = time;            
        } else {
            changeLocation();
        }       
    }
    
    
    /**
     * Updates the location this image view should be at then relocates it.
     */
    public void changeLocation() {
        updateLocation();
        this.imageView.relocate(x, y);
        this.imageView.setRotate(GameMath.calculateAngle(
                                    x, y, 
                                    GameManager.mainPlayer.getCenterX(), 
                                    GameManager.mainPlayer.getCenterY()));
    }
    
    
    /** 
     * Calculate the rise and run to the main player. This is individualized for
     * each Enemy to avoid threading issues. 
     * 
     * @return 
     */
    private double[] calculateNextPoint() {
        double rise = GameManager.mainPlayer.getCenterY() - y,
                run = GameManager.mainPlayer.getCenterX() - x;
        
        boolean nRise = false, nRun = false;
        
        if (rise < 0) {
            rise *= -1;
            nRise = true;
        }
        
        if (run < 0) {
            run *= -1;
            nRun = true;
        }
        
        if (Math.max(rise, run) == rise) {
            run = (scaledDistance * run) / rise;
            rise = scaledDistance;
        } else {
            rise = (scaledDistance * rise) / run;
            run = scaledDistance;
        }
        
        if (nRise) rise *= -1;
        if (nRun) run *= -1;
        
        return new double[]{rise,run};
    }
    
    
    /**
     * Updates this enemies location. 
     */
    private void updateLocation() {
        double[] temp = calculateNextPoint();
        x += temp[1];
        y += temp[0];
    }    
    
    
    /**
     * Deducts this enemy's health. If the health is at zero, it will remove this
     * enemy from the living enemies list and add it to the dead enemies list. 
     */
    public void deductHealth() {
        this.currentHealth--;
        if (this.currentHealth == 0) {
            this.imageView.setImage(Settings.getDeadPlayerImage());
            GameManager.amountKilled++;
            GameManager.removePlayer(this);
            GameManager.deadEnemies.add(this);
        }
    }            
}
