/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxlab;

import fxlab.util.DialogUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.SwingDispatchService;

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

    @Override
    public void init() throws Exception {
        super.init(); 
        
        try {
            Logger logger= Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.WARNING);
            logger.setUseParentHandlers(false);
            
            GlobalScreen.registerNativeHook();
            GlobalScreen.setEventDispatcher(new SwingDispatchService());
        } catch (NativeHookException ex) {
            Logger.getLogger(FXLab.class.getName()).log(Level.SEVERE, null, ex);
            DialogUtil.showException("There was a problem registering the native hook.", 
                    ex);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        
        try {
            if (GlobalScreen.isNativeHookRegistered())
                GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            Logger.getLogger(FXLab.class.getName()).log(Level.SEVERE, null, ex);
            DialogUtil.showException("There was a problem unregistering the native hook.", 
                    ex);
        }
    }
}