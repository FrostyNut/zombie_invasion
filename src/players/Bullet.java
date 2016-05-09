package players;

import gamemanager.GameManager;
import gamemanager.GameMath;
import gamemanager.RuntimeSettings;
import gamemanager.Settings;
import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * The bullet class will contain an animation timer that will move an image view
 * until it has hit an object or gone off the map.
 *
 * @author Jackie Chan
 * Apr 20, 2016
 */
public class Bullet {

    
    /** The bullet's current x and y coordinates. */
    private double x, y;
    
    
    /** The angle this bullet should be rotated at. */
    private final double r;
    
    
    /** The first index contains the rise of the line; the second, run. */
    private final double[] slope;
    
    
    /** Contains an image of the bullet. */
    private final ImageView bulletView;    
    
    
    /**
     * Constructs a new Bullet object that has a specified (x,y) coordinate,
     * a destination (x,y) coordinate, and an angle of rotation.
     * 
     * @param x     The starting x-coordinate of the bullet.
     * @param y     The starting y-coordinate of the bullet.
     * @param dX    The destination x coordinate of the bullet.
     * @param dY    The destination y coordinate of the bullet.
     * @param r     The angle the bullet should be at.
     */
    public Bullet(double x, double y, double dX, double dY, double r) {
        this.x      = x; 
        this.y      = y;
        
        this.slope  = GameMath.calculateSlope(x, dX, y, dY);
        
        this.bulletView = new ImageView(Settings.BULLET);
        
        this.r = r;
    }
    
    
    /**
     * Will animate the bullet.
     * 
     * @param pane      The pane to draw the bullet in.
     */
    public void start(Pane pane) {
        
        pane.getChildren().add(this.bulletView);

        // Roate the bullet and move it to the starting position.
        this.bulletView.setRotate(r);
        this.bulletView.relocate(x,y);        
        
        /*
            Use the AnimationTimer to animate the bullet. Since multithreading
            would be too complicated, just manually manage your time. Also, 
            time is managed manually because you can't use Thread.sleep() or 
            wait() on a JavaFX thread, unless you are crazy jimmy.
        */
        AnimationTimer timer = new AnimationTimer() {

            long previousTime = 0;
            
            /*
                onDestroy is only true when this bullet has collided with
                something.
            */
            boolean onDestroy = false;
            
            @Override
            public void handle(long now) {
                
                long time = now / 1000000;
                
                if (!onDestroy) {

                    if (time - previousTime >= Settings.BULLET_MOVEMENT_DELAY
                            || previousTime == 0) {
                                                                                               
                        x += slope[1]; y += slope[0];
                        
                        if (x > 0 && x < RuntimeSettings.getMaxBulletX()
                                && y > 0 && y < RuntimeSettings.getMaxBulletY()) {

                            bulletView.relocate(x,y);
                            previousTime = time;

                            for (Enemy e : GameManager.enemies) {
                                if (bulletView.getBoundsInParent().intersects(e.getImageView().getBoundsInParent())) {
                                    this.onDestroy = true;
                                    bulletView.relocate(e.getCenterX()-15, e.getCenterY()-15);
                                    bulletView.setImage(Settings.EXPLOSION);
                                    e.deductHealth();
                                }
                            }                            
                        } else {

                            this.onDestroy = true;

                            bulletView.setImage(Settings.EXPLOSION);
                        }
                    }

                } else if (time - previousTime >= Settings.EXPLOSION_DURATION) {
                    this.stop();
                    pane.getChildren().remove(bulletView);
                }

            }
        };
        
        System.out.println("Bullet fired");
        timer.start();
    }
    
}
