package gamemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import players.Enemy;
import players.MainPlayer;

/**
 *
 *
 * @author Jackie Chan
 * May 2, 2016
 */
public class GameManager {

    public static MainPlayer mainPlayer;
    public static List<Enemy> enemies;

    private static boolean gameActive = false;
        
    private static Stage primaryStage;
    private static Label gameStats;
    private static Pane playerField;
    
    private static Scene gameplayScene;
    
    private static Input input;
    
    private static AnimationTimer mainUpdateTimer;
    private static AnimationTimer enemySpawnTimer;

    /**
     * Private GameManager constructor method so the class cannot be instantiated.
     */
    private GameManager(){}
    
    
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }


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
        
        mainUpdateTimer = new AnimationTimer() {
            
            long previousTime = 0;
            
            @Override
            public void handle(long now) {
                long time = now / 1000000;
                
                if(time - previousTime >= Settings.REFRESH_RATE) {
                    List<Enemy> temp = new ArrayList<>(enemies);
                    for(Enemy e : temp) {
                        e.attackPlayer(now);
                    }
                    gameStats.setText("Health:\t"+mainPlayer.getHealth());
                    previousTime = time;
                }                                
                
                mainPlayer.changeValues();
                mainPlayer.move();
                mainPlayer.updateUI();
            }
        };
        
        startNewGame();
        
    }
    
    /**
     * Sets the scene to the main game play scene.
     */
    private static void startNewGame() {  
        
        primaryStage.setScene(gameplayScene);
        playerField.getChildren().clear();
        
        enemies.clear();
        
        spawnPlayer();
        spawnEnemies();
        
        input.addListeners();                                
        
        gameActive = true;               
        
        mainUpdateTimer.start();
    }


    /**
     * Stops every timer that exists in the game, sets the value of gameActive
     * to false, and clears the screen of any objects that inhabit it.
     */
    public static void stopGame() {
        gameActive = false;
        input.removeListeners();
        mainUpdateTimer.stop();
        enemySpawnTimer.stop();
    }
    
    
    public static void restartGame() {
        gameActive = true;
        input.addListeners();
        startNewGame();
    }


    /**
     * Does the same thing as the stopGame() method but does not clear objects
     * off of the screen. Later on, will set an overlay view to display buttons
     * to handle the user's next action.
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
     * Sets the game status back to active. Clears the game play screen of
     * anything that obstructs the view of the main player. Will also start each
     * timer that is in the timers array list.
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
     * Spawns the main player at a random place in the game play screen.
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
     * Spawns 10 enemies (value to be adjusted later. Plus, it will continuously
     * spawn enemies later, so that needs to be factored in with an animation
     * timer.
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
                        Enemy e = new Enemy(playerField,
                                Settings.getEnemyImage(1),
                                rand.nextInt(RuntimeSettings.getMaxPlayerSpawnX()),
                                rand.nextInt(RuntimeSettings.getMaxPlayerSpawnY()),
                                0, 0, 0, 0, 3, 2);
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
    

    public static boolean gameActive() {return gameActive;}
    
    public static void removePlayer(Enemy player) {
        List<Enemy> copyOfEnemies = new ArrayList<>(enemies);
        copyOfEnemies.remove(player);
        enemies = copyOfEnemies;
    }
}