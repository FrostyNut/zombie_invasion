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
import javafx.stage.Stage;

/**
 *
 *
 * @author Jackie Chan
 * May 5, 2016
 */
public class SceneCreator {


    public static Scene createPauseMenu() {
        
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
//               GameManager.displayMainMenu();
           }
        });

        // Put the grid pane in the center of the scene and add the buttons.
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        
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


    public static Scene createStartScene() {

        Button btn = new Button();
        btn.setText("Start!");

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 800, 600);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameManager.loadGame();
            }
        });

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
        stats.setPrefSize(125, 25);
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
//                GameManager.restartGame();
            }
        });

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
//                GameManager.displayMainMenu();
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