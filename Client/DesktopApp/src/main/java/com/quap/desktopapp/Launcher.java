package com.quap.desktopapp;


import com.quap.controller.scene.ConnectionWindowController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void init() {
        ConnectionWindowController init = new ConnectionWindowController();
        init.connect();
        init.launchWindow();
        }

    @Override
    public void start(Stage primaryStage) { }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", LauncherPreloader.class.getCanonicalName());
        launch(args);
    }
}
