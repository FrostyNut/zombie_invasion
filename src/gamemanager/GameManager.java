package gamemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import players.Enemy;
import players.MainPlayer;


/**
 * The GameManager class will manage the Major aspects of the game. These include
 * updating locations, removing dead bodies, and handling game state changes. 
 * Game state changes include pause, resume, stop, and quit. 
 *
 * @author Jackie Chan
 * May 2, 2016
 */
public class GameManager {

    
    /** 
     * The main player in the game. The user will be controlling this player. 
     * See the MainPlayer and Player classes for more detail. 
     * 
     * @see players.MainPlayer
     * @see players.Player
     */
    public static MainPlayer mainPlayer;
    
    
    /** Contains the enemies that are alive. */
    public static List<Enemy> enemies;
    
    
    /** Contains the dead enemies. */
    public static List<Enemy> deadEnemies;

    
    /** Contains a value determining whether game play is active. */
    private static boolean gameActive = false;

    
    /** The Stage that holds the Scenes displayed to the user. */
    private static Stage primaryStage;
    
    
    /** 
     * Displays the current health of the main player and the amount of enemies 
     * killed.
     */
    private static Label gameStats;
    
    
    /** The Pane the main player and enemies are added to. */
    private static Pane playerField;

    
    /** The scene displayed when game play starts. */
    private static Scene gameplayScene;

    
    /** The Input class is the input handler for the main player. */
    private static Input input;
    

    /** 
     * The animation timer that will update main player and enemy locations. It
     * will also decide when to clear the dead bodies from the screen (every 
     * 10000 milliseconds) and when to move the enemies. 
     */
    private static AnimationTimer mainUpdateTimer;
    
    
    /** The animation timer that will control when enemies spawn. */
    private static AnimationTimer enemySpawnTimer;

    
    /** The amount of enemies killed. */
    public static int amountKilled = 0;

    
    /**
     * Private constructor so this class can't be instantiated.
     */
    private GameManager(){}


    /**
     * Sets the Stage to use during the application's runtime. The primaryStage
     * is used to display Scenes to the user.
     * 
     * @param stage     The primary stage.
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }


    /**
     * Sets the scene to the main game scene. It will also create the Animation 
     * Timer used for updating enemy and player locations, and will instantiate
     * the lists used to keep track of the currently, alive and dead, enemies.
     */
    public static void loadGame() {
        if(primaryStage == null)
            throw new NullPointerException("Primary Stage is Null.");

        RuntimeSettings.loadRuntimeSettings(800, 600);
        
        Object[] graphicalComponents = SceneCreator.createGamePlayScene();

        gameplayScene   = (Scene)graphicalComponents[0];
        playerField     = (Pane)graphicalComponents[1];
        gameStats       = (Label)graphicalComponents[2];

        input = new Input(gameplayScene, playerField);

        enemies = new ArrayList<>();
        deadEnemies = new ArrayList<>();

        mainUpdateTimer = new AnimationTimer() {

            long previousTime = 0;  // Used for updating enemy location.
            long previousTime2 = 0; // Used for cleaning the dead enemy bodies.

            /*
                This will control all animations pertaining to the main player and
                to the enemies.
            */
            @Override
            public void handle(long now) {                
                long time = now / 1000000;  // Manually handle time.

                if(mainPlayer.getHealth() <= 0) {
                    stopGame();
                }

                // Update the enemies and the plaeyr stats.
                if(time - previousTime >= Settings.REFRESH_RATE) {
                    List<Enemy> temp = new ArrayList<>(enemies);
                    for (Enemy e : temp) {
                        e.attackPlayer(now);
                    }

                    gameStats.setText("Health:\t"+mainPlayer.getHealth()
                                        + "\tAmount Killed:\t"+amountKilled);
                    previousTime = time;
                }

                // Remove all dead enemies from the game play field.
                if(time - previousTime2 >= 10000) {
                    List<Enemy> temp = new ArrayList<>(deadEnemies);
                    for (Enemy e : temp) {
                        playerField.getChildren().remove(e.getImageView());
                        deadEnemies.remove(e);
                    }
                    previousTime2 = time;
                }

                // Update the main player.
                mainPlayer.changeValues();
                mainPlayer.move();
                mainPlayer.updateUI();
            }
        };

        startNewGame();
    }

    
    /**
     * Used to setup a new game. This should always be called when using loadGame() 
     * is too much, like restarting the game after the main player died. 
     */
    private static void startNewGame() {
        primaryStage.setScene(gameplayScene);
        
        playerField.getChildren().clear();
        enemies.clear();
        
        spawnPlayer();
        spawnEnemies();

        input.resetSettings();
        input.addListeners();

        amountKilled    = 0;
        gameActive      = true;

        mainUpdateTimer.start();
    }


    /**
     * Prevents the game scene from being updated and sets the scene to the 
     * "Game Over" scene. 
     */
    public static void stopGame() {
        gameActive = false;
        input.removeListeners();
        mainUpdateTimer.stop();
        enemySpawnTimer.stop();       
        primaryStage.setScene(SceneCreator.createGameOverScene());
    }


    /**
     * Starts a new game.
     */
    public static void restartGame() {
        gameActive = true;
        input.addListeners();       
        startNewGame();
    }


    /**
     * Pauses the game.
     */
    public static void pauseGame() {
        gameActive = false;
        input.removeListeners();
        mainUpdateTimer.stop();
        enemySpawnTimer.stop();
        primaryStage.setScene(SceneCreator.createPauseMenu());
        System.out.println("GameManager:\tGame Paused.");
    }


    /**
     * Resumes the game.
     */
    public static void resumeGame() {
        gameActive = true;
        primaryStage.setScene(gameplayScene);
        input.addListeners();
        mainUpdateTimer.start();
        enemySpawnTimer.start();
        System.out.println("GameManager:\tGame Resumed.");
    }


    /**
     * Sets the Scene to the "Main Menu" scene.
     */
    public static void goToMainMenu() {
        primaryStage.setScene(SceneCreator.createStartScene());
    }


    /**
     * Spawns the main player at a random place in the game play scene. 
     */
    private static void spawnPlayer() {
        Random rand = new Random();
        mainPlayer = new MainPlayer(playerField,
                                    Settings.getMainPlayerImage(),
                                    rand.nextInt(RuntimeSettings.getMaxPlayerSpawnX()),
                                    rand.nextInt(RuntimeSettings.getMaxPlayerSpawnY()),
                                    0,0,0,0, input);
        playerField.getChildren().add(mainPlayer.getImageView());   
    }


    /**
     * Continuously spawns a random amount of enemies (between 1 and 10 inclusive)
     * at random places on the game play scene. There are only three types of 
     * enemy that can be spawned. See the Enemy class for more information on the
     * types. 
     * @see players.Enemy
     */
    private static void spawnEnemies() {
        Random rand = new Random();

        enemySpawnTimer = new AnimationTimer() {
            
            long previousTime = 0;

            @Override
            public void handle(long now) {
                long time = now / 1000000;
                
                if(time - previousTime >= 5000) {
                    for (int i = 0; i < rand.nextInt(10) + 1; i++) {
                        Enemy e;

                        switch(rand.nextInt(3)) {

                            case 0:
                                e = new Enemy(playerField,
                                        Settings.getEnemyImage(1),
                                        rand.nextInt(RuntimeSettings.getMaxPlayerSpawnX()),
                                        rand.nextInt(RuntimeSettings.getMaxPlayerSpawnY()),
                                        0, 0, 0, 0, 2, 1);
                                break;

                            case 1:
                                 e = new Enemy(playerField,
                                        Settings.getEnemyImage(2),
                                        rand.nextInt(RuntimeSettings.getMaxPlayerSpawnX()),
                                        rand.nextInt(RuntimeSettings.getMaxPlayerSpawnY()),
                                        0, 0, 0, 0, 3, 2);
                                break;

                            case 2:
                                e = new Enemy(playerField,
                                        Settings.getEnemyImage(3),
                                        rand.nextInt(RuntimeSettings.getMaxPlayerSpawnX()),
                                        rand.nextInt(RuntimeSettings.getMaxPlayerSpawnY()),
                                        0, 0, 0, 0, 5, 4);
                                break;

                            default:
                                e = new Enemy(playerField,
                                        Settings.getEnemyImage(1),
                                        rand.nextInt(RuntimeSettings.getMaxPlayerSpawnX()),
                                        rand.nextInt(RuntimeSettings.getMaxPlayerSpawnY()),
                                        0, 0, 0, 0, 2, 1);
                                break;

                        }
                        enemies.add(e);
                        e.changeLocation();
                        playerField.getChildren().add(e.getImageView());
                    }
                    previousTime = time;
                }
            }
        };

        enemySpawnTimer.start();
    }


    /**
     * Returns true when the game is being played; otherwise will return false.
     * 
     * @return  true when the game is being played; otherwise will return false.
     */
    public static boolean gameActive() {return gameActive;}

    
    /**
     * Will remove an enemy from the enemies list. 
     * 
     * @param enemyToRemove     The enemy to remove.
     */
    public static void removePlayer(Enemy enemyToRemove) {
        List<Enemy> copyOfEnemies = new ArrayList<>(enemies);
        copyOfEnemies.remove(enemyToRemove);
        enemies = copyOfEnemies;
    }
}