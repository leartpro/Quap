package com.quap.desktopapp;


import com.quap.controller.scene.ConnectionWindowController;
import javafx.application.Application;
import javafx.stage.Stage;


/*
 * Launcher application class.
 */
public class Launcher extends Application {

    public static Stage primaryStage = null;

    @Override
    public void init() {
        ConnectionWindowController init = new ConnectionWindowController();
        init.openConnection();
        init.connect();
        init.confirmConnection();
    }

    @Override
    public void start(Stage primaryStage) {
        Launcher.primaryStage = primaryStage;

    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", LauncherPreloader.class.getCanonicalName());
        launch(args);
    }
}
