package com.quap.controller.scene;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/*
    The loading screen of this application
 */
public class ConnectionWindowController implements Initializable {

    public Label lblLoading;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public String checkFunctions() {

        final String[] message = {""};

        Thread t1 = new Thread(() -> {
            message[0] = "First Function";
            Platform.runLater(() -> lblLoading.setText(message[0]));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            Platform.runLater(() -> lblLoading.setText(message[0]));
            message[0] = "Second Function";
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

        return message[0];
    }
}
