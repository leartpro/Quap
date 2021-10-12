package com.quap.desktopapp;


import com.quap.controller.scene.ConnectionWindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/*
 * Launcher application class.
 */
public class Launcher extends Application {

    public static Stage primaryStage = null;
    public static Scene primaryScene = null;

    @Override
    public void init() {
        ConnectionWindowController init = new ConnectionWindowController();
        init.checkFunctions();
    }

    @Override
    public void start(Stage primaryStage) {
        Launcher.primaryStage = primaryStage;

    }

    public static void main(String[] args) {

    }
}
