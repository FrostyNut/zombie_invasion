/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undesirable_invasion;

import gamemanager.GameManager;
import gamemanager.SceneCreator;
import gamemanager.SoundManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author frost
 */
public class Undesirable_Invasion extends Application {
    
    @Override
    public void start(Stage primaryStage) { 
        if(System.getProperty("os.name").toLowerCase().contains("window")) {
            new SoundManager().playSound();    
            System.out.println("Started sound.");
        } else {
            System.out.println("Cannot play sound on "+System.getProperty("os.name"));
        }
        GameManager.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Undesirable Invasion");        
        primaryStage.setScene(SceneCreator.createStartScene());
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
