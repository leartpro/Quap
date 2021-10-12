package com.quap.controller.scene;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*
    The loading screen of this application
 */
public class ConnectionWindowController implements Initializable {

    @FXML
    Label lblLoading;
    public static Label loadingLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingLabel=lblLoading;
    }

    public String checkFunctions() {

        final String[] message = {""};

        Thread t1 = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText(message[0]));
            message[0] = "First Function";
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            message[0] = "Second Function";
            Platform.runLater(() -> loadingLabel.setText(message[0]));
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    Stage stage = new Stage();
                    Parent root = null;
                        root = FXMLLoader.load(getClass().getResource("/com/quap/desktopapp/scene/main-window.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        Thread t3 = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText(message[0]));
            message[0] = "Open Main Stage";
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t3.start();
        try {
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return message[0];
    }
}
