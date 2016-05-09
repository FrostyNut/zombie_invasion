package gamemanager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * The SceneCreator creates scenes that will be used during the runtime of the
 * program. Think of scenes as different screens in the game. For example, the
 * scene of which we play the game in is called the "Main Game Scene" and the
 * scene where we click the start button is called the "Start Scene". This makes 
 * it much easier so there is less "Graphical" code in the GameManager and we can
 * better read the more "logical" code. In other words, there is less visual 
 * noise. 
 *
 * @author Jackie Chan
 * May 5, 2016
 */
public class SceneCreator {


    /**
     * Creates the "Game Paused Scene". The game paused scene only occurs when 
     * the user presses the key "T". It will give the user the options to resume
     * game play or go to the main menu. 
     * 
     * @return  A scene containing two buttons and the text "Paused".
     */
    public static Scene createPauseMenu() {
        
        Text paused = new Text("Paused");
        paused.setFont(new Font(50));
        
        // Resume button for when user wants to resume gameplay.
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GameManager.resumeGame();
            }
        });

        // Main menu button for when user wants to go to the main menu.
        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent e) {
               GameManager.goToMainMenu();
           }
        });

        // Put the grid pane in the center of the scene and add the buttons.
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        
        gridPane.add(paused, 0, 0);
        GridPane.setHalignment(paused, HPos.CENTER);
        GridPane.setMargin(paused, new Insets(5, 7, 5, 7));
        gridPane.add(resumeButton, 0, 1);
        GridPane.setHalignment(resumeButton, HPos.CENTER);
        GridPane.setMargin(resumeButton, new Insets(5,7,5,7));
        gridPane.add(mainMenuButton, 0, 2);
        GridPane.setHalignment(mainMenuButton, HPos.CENTER);
        GridPane.setMargin(mainMenuButton, new Insets(5,7,5,7));

        return new Scene(gridPane,
                            RuntimeSettings.getWidth(),
                            RuntimeSettings.getHeight());
    }


    /**
     * Creates a scene containing the Start button. 
     * @return 
     */
    public static Scene createStartScene() {

        Text zombieInvasion = new Text("Zombie Invasion");
        zombieInvasion.setFont(new Font(50));
        
        Button btn = new Button("Start!");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameManager.loadGame();
            }
        });
        
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        
        gridPane.add(zombieInvasion, 0, 0);
        GridPane.setHalignment(zombieInvasion, HPos.CENTER);
        GridPane.setMargin(zombieInvasion, new Insets(5,7,5,7));

        gridPane.add(btn, 0, 1);
        GridPane.setHalignment(btn, HPos.CENTER);
        GridPane.setMargin(btn, new Insets(5,7,5,7));        
        
        Scene scene = new Scene(gridPane, 800, 600);

        return scene;
    }


    /**
     * Creates the main game play window (scene). It will return an object array
     * containing the following elements in the following order:
     * 1. The Scene that needs to be set to the primaryStage (Stage).
     * 2. The Pane that holds the players and enemies.
     * 3. The Label that contains the player's health.
     *
     * @return      An object array containing the Scene to be set, the Pane that
     *              holds the players and enemies, and the Label that displays
     *              the player's health.
     */
    public static Object[] createGamePlayScene() {

        HBox statusBox = new HBox();
        statusBox.setPadding(new Insets(15, 12, 15, 12));
        statusBox.setSpacing(10);
        statusBox.setStyle("-fx-background-color: #FFFFFF;");

        Label stats = new Label("Health: ");
        stats.setPrefSize(400, 25);
        statusBox.getChildren().add(stats);

        Pane playerField = new Pane();

        BorderPane borderPane = new BorderPane();
        borderPane.setBottom(stats);
        borderPane.setCenter(playerField);

        Scene scene = new Scene(borderPane,
                                RuntimeSettings.getWidth(),
                                RuntimeSettings.getHeight(),
                                Settings.BACKGROUND);

        return new Object[]{scene, playerField, stats};
    }


    /**
     * Creates a scene that contains the text "Game Over", and two buttons labeled
     * restart and quit. The restart button will restart the game play. The quit
     * button will exit to the home screen.
     *
     * @return  A scene containing the text "Game Over", and two buttons labeled
     *          restart and quit.
     */
    public static Scene createGameOverScene() {
        Text gameOver = new Text("Game Over");
        gameOver.setFont(new Font(50));

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GameManager.restartGame();
            }
        });

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                GameManager.goToMainMenu();
            }
        });


        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(gameOver, 0, 0);
        GridPane.setHalignment(gameOver, HPos.CENTER);
        GridPane.setMargin(gameOver, new Insets(5,7,5,7));
        gridPane.add(restartButton, 0, 1);
        GridPane.setHalignment(restartButton, HPos.CENTER);
        GridPane.setMargin(restartButton, new Insets(5,7,5,7));
        gridPane.add(mainMenuButton, 0, 2);
        GridPane.setHalignment(mainMenuButton, HPos.CENTER);
        GridPane.setMargin(mainMenuButton, new Insets(5,7,5,7));

        return new Scene(gridPane,
                            RuntimeSettings.getWidth(),
                            RuntimeSettings.getHeight());
    }
}