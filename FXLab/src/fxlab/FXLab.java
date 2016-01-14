/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author srey
 */
public class FXLab extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/fxlab/ui/FXMLLab.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("FXLab");
            primaryStage.show();
            
            System.out.println(String.format("OS Name: %s", System.getProperty("os.name")));
        } catch (IOException ex) {
            Logger.getLogger(FXLab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}