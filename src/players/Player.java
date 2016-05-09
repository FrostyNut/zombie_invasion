package players;

import gamemanager.Settings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


/**
 * The Player class will be extended by enemies and the main player. It provides
 * an outline of what a player or enemy should be.
 *
 * @author Jackie Chan
 * Apr 17, 2016
 */
public abstract class Player {
    
    /** The image view that contains this player's image. */
    public ImageView imageView;
    
    /** The image of this player. */
    public Image image;
    
    /** The x coordinate of the player. */
    public double x;
    
    /** The y coordinate of the player. */
    public double y;
    
    /** The angle of rotation in radians of the player. */
    public double r;
    
    /** The velocity on the x-axis. */
    public double velX;
    
    /** The velocity on the y-axis. */
    public double velY;
    
    /** The velocity rotated. */
    public double velR;
    
    /** The player's width. */
    public double w;
    
    /** The player's height. */
    public double h;
    
    /** The pane that the player is in. */
    public Pane pane;    
    
    
    /**
     * Creates a new Player. 
     * 
     * @param pane      The Pane the player should be drawn in.
     * @param img       The player's image.
     * @param x         The player's starting x-coordinate.
     * @param y         The player's starting y-coordinate.
     * @param r         The player's starting rotation.
     * @param velX      The player's velocity on the x axis.
     * @param velY      The player's velocity on the y axis. 
     * @param velR      The velocity of rotation.
     */
    public Player(Pane pane, Image img, double x, double y, double r, double velX, double velY, double velR) {       
        
        this.pane           = pane;
        this.image          = img;
        
        this.x              = x;
        this.y              = y;
        this.r              = r;
        
        this.velX           = velX;
        this.velY           = velY;
        this.velR           = velR;
        
        this.w              = Settings.PLAYER_WIDTH;
        this.h              = Settings.PLAYER_HEIGHT;
        
        this.imageView = new ImageView();
        this.imageView.setImage(this.image);        
    }
    
    
    /**
     * Relocates the ImageView for this class to it's new x and y location.    
     */
    public void updateUI() {
        this.imageView.relocate(x, y);
        this.imageView.setRotate(r);
    }
    
    
    /**
     * Changes the x and y coordinates.
     */
    public void move() {
        x += velX;
        y += velY;
    }
    

    /*
        Most of the methods below set and return values in this class.
    */
    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }
    
    public double getCenterX() {
        return x + (Settings.PLAYER_WIDTH / 2);
    }
    
    public double getCenterY() {
        return y + (Settings.PLAYER_HEIGHT / 2);
    }
    
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ImageView getImageView() {
        return imageView;
        
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getVelR() {
        return velR;
    }

    public void setVelR(double velR) {
        this.velR = velR;
    }

    public Pane getPane() {
        return pane;
    }

    public void setParentScene(Pane pane) {
        this.pane = pane;
    }
}
