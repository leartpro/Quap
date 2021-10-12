package com.quap.desktopapp;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LauncherPreloader extends Preloader {

    private Stage preLoaderStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preLoaderStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/com/quap/desktopapp/scene/connection-window.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        super.handleStateChangeNotification(info);
        if(info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preLoaderStage.hide();
        }
    }
}
