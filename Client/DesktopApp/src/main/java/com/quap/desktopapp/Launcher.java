package com.quap.desktopapp;


import com.quap.client.data.ConfigReader;
import com.quap.controller.scene.ConnectionWindowController;
import javafx.application.Application;
import javafx.stage.Stage;


/*
 * Launcher application class.
 */
public class Launcher extends Application {

    //TODO: check system vars, folder systems, hardware usw...
    @Override
    public void init() {
        ConnectionWindowController init = new ConnectionWindowController();
        init.connect();
        //init.confirmConnection();
        init.launchWindow();
        ConfigReader configReader = new ConfigReader("anonym");
        configReader.createUser();
        }

        //TODO: find better solution
    @Override
    public void start(Stage primaryStage) {

    }


    public static void main(String[] args) {
        System.setProperty("javafx.preloader", LauncherPreloader.class.getCanonicalName());
        launch(args);
    }
}
