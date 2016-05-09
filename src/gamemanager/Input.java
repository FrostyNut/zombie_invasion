package gamemanager;

import java.util.BitSet;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import players.Bullet;


/**
 * Processes events that pertain to the functionality of the main player.
 *
 * @author Jackie Chan Apr 17, 2016
 */
public class Input {

    
    /**
     * Will hold if a key is being pressed or not.
     */
    private BitSet keyboardBitSet = new BitSet();

    
    /**
     * Key codes for the WASD keys.
     */
    private final KeyCode upKey = KeyCode.W;
    private final KeyCode leftKey = KeyCode.A;
    private final KeyCode downKey = KeyCode.S;
    private final KeyCode rightKey = KeyCode.D;

    
    /**
     * The angle the main player should rotate to.
     */
    private double playerAngle = 0;

    
    /**
     * The scene that the listeners should be added to.
     */
    private Scene scene;

    
    /**
     * The Pane that holds the main player, bullets, and enemies.
     */
    private Pane playerField;    
    
    
    /**
     * A boolean variable that tracks if the mouse is being pressed.
     */
    private boolean isShooting = false;
    
                    
    /**
     * When a user presses a mouse button, this MouseEvent will have its value
     * set to the MouseEvent corresponding with the press.
     */
    private MouseEvent mousePressedEvent;
    private MouseEvent mouseMovedEvent;
    
    
    /**
     * Will fire bullets.
     */
    private AnimationTimer gunshotAnimation;                

    
    /**
     * Constructor method for the Input class. This class will handle all events
     * that pertain to the functionality of the main player.
     *
     * @param scene The scene where the listeners should be added.
     * @param playerField The Pane that the player is on.
     */
    public Input(Scene scene, Pane playerField) {
        this.scene = scene;
        this.playerField = playerField;
        setupBulletTimer();
    }

    
    /*
       Sets the bit at position e.getCode().ordinal() to true (1).
     */
    private EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            keyboardBitSet.set(e.getCode().ordinal(), true);
            
            if(mouseMovedEvent != null)             
                updateAngle(mouseMovedEvent);
            if(e.getCode() == KeyCode.T) {
                GameManager.pauseGame();
            }
        }
    };

    
    /*
        Sets the bit at position e.getCode().ordinal() to false (0).
     */
    private EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent e) {
            keyboardBitSet.set(e.getCode().ordinal(), false);                
        }

    };
    
    
    /**
     * Starts the bullet firing animation timer. This timer will fire bullets at
     * the specified firing rate.
     */
    private EventHandler<MouseEvent> mousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {  
            System.out.println("Input:\tMouse Pressed Event Fired");
            mousePressedEvent = e;
            if(!isShooting) {
                isShooting = true;
                if(GameManager.gameActive()) gunshotAnimation.start();
                System.out.println("Started shooting");
            }
            
        }
        
    };
    
    
    /**
     * Updates the current angle that the player should be rotated to. This is 
     * only called if no mouse button is being pressed.
     */
    private EventHandler<MouseEvent> mouseMovedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            mouseMovedEvent = e;
            updateAngle(e);
        }
    };
    
    
    /**
     * Stops the AnimationTimer that is firing bullets.
     */
    private EventHandler<MouseEvent> mouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        
        @Override
        public void handle(MouseEvent e) {
            System.out.println("Input:\tMouse Released Event Fired");
            gunshotAnimation.stop();
            isShooting = false;
            System.out.println("Stopped shooting.");
        }
    };
    
    
    /**
     * Sets up the AnimationTimer that will fire bullets.
     */
    public void setupBulletTimer() {
        gunshotAnimation = new AnimationTimer() {
            
            long previousTime = 0;
            
            @Override
            public void handle(long now) {
                if (GameManager.gameActive()) {
                    
                    long time = now / 1000000;
                    
                    if (time - previousTime >= Settings.BULLET_FIRING_DELAY) {
                        new Bullet(GameManager.mainPlayer.getCenterX(), 
                                    GameManager.mainPlayer.getCenterY(), 
                                    mousePressedEvent.getX(), 
                                    mousePressedEvent.getY(),
                                    GameMath.calculateAngle(GameManager.mainPlayer.getCenterX(),
                                                            GameManager.mainPlayer.getCenterY(),
                                                            mousePressedEvent.getX(),
                                                            mousePressedEvent.getY()))
                                    .start(playerField);                    
                        previousTime = time;
                    }
                } else {
                    this.stop();
                    isShooting = false;
                }
            }
        };
    }     
    

    /**
     * Call this when the game begins.
     */
    public void addListeners() {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedEventHandler);
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, mousePressedEventHandler);
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedEventHandler);
        scene.addEventFilter(MouseEvent.ANY, mouseMovedEventHandler);
    }
    
    
    public void removeListeners() {
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
        scene.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedEventHandler);
        scene.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mousePressedEventHandler);        
        scene.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedEventHandler);
        scene.removeEventFilter(MouseEvent.ANY, mouseMovedEventHandler);
    }


    /**
     * Returns if the main player should move up.
     *
     * @return if the main player should move up.
     */
    public boolean isMoveUp() {
        return keyboardBitSet.get(upKey.ordinal()) && !keyboardBitSet.get(downKey.ordinal());
    }

    /**
     * Returns if the main player should move down.
     *
     * @return if the main player should move down.
     */
    public boolean isMoveDown() {
        return keyboardBitSet.get(downKey.ordinal()) && !keyboardBitSet.get(upKey.ordinal());
    }

    /**
     * Returns if the main player should move left.
     *
     * @return if the main player should move left.
     */
    public boolean isMoveLeft() {
        return keyboardBitSet.get(leftKey.ordinal()) && !keyboardBitSet.get(rightKey.ordinal());
    }

    /**
     * Returns if the main player should move right.
     *
     * @return if the main player should move right.
     */
    public boolean isMoveRight() {
        return keyboardBitSet.get(rightKey.ordinal()) && !keyboardBitSet.get(leftKey.ordinal());
    }

    /**
     * Updates the angle that the main player should be at.
     *
     * @param e The mouse event that holds the mouse pointer's x,y coordinates.
     */
    public void updateAngle(MouseEvent e) {
        playerAngle = GameMath.calculateAngle(
                GameManager.mainPlayer.getCenterX(), 
                GameManager.mainPlayer.getCenterY(), 
                e.getX(), e.getY());
    }

    /**
     * Returns the angle that the main player should be at.
     *
     * @return the angle that the main player should be at.
     */
    public double getAngle() {
        return playerAngle;
    }
    
    public void resetSettings() {
        gunshotAnimation.stop();
        this.isShooting = false;
    }

}
