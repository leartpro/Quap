package com.quap.desktopapp;


import com.quap.controller.scene.ConnectionWindowController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Die Klasse stellt die Main Klasse des Clients dar, welche die main-Methode enthält
 *
 */
public class Launcher extends Application {

    /**
     * Die Methode sorgt für die Initialisierung im Verbindungsfenster
     */
    @Override
    public void init() {
        ConnectionWindowController init = new ConnectionWindowController();
        init.connect();
        init.launchWindow();
        }

    @Override
    public void start(Stage primaryStage) {}

    /**
     * Beim Methodenaufruf, wird der Preloader des Clients geladen
     * @param args Arguemte der Client Anwendung (Keine Argumente erwartet)
     */
    public static void main(String[] args) {
        System.setProperty("javafx.preloader", LauncherPreloader.class.getCanonicalName());
        launch(args);
    }
}
