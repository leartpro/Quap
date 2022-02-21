package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.controller.VistaController;
import com.quap.desktopapp.LauncherPreloader;
import com.quap.utils.WindowMoveHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionWindowController implements Initializable {
    private Client client;

    @FXML
    private Label lblLoading;

    @FXML
    private ProgressBar progressBar;

    private static Label loadingLabel;
    private static ProgressBar prBar;
    double progress;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingLabel = lblLoading;
        prBar = progressBar;
    }

    public void connect(String adress) {
        increaseProgress();
        Platform.runLater(() -> loadingLabel.setText("Open Connection"));
        try {
            if (adress != null) {
                client = new Client(adress, 8192);
            } else {
                client = new Client("192.168.178.43", 8192);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void increaseProgress() {
        if (progress < 1) {
            for (int i = 0; i < 50; i++) {
                progress += 0.01;
                prBar.setProgress(progress);
                //System.out.println(Double.toString(Math.round(progress * 100)) + "%");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void launchWindow() {
        increaseProgress();
        Platform.runLater(() -> loadingLabel.setText("Loading Login"));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage stage = new Stage();
                Parent root = null;
                FXMLLoader loader = new FXMLLoader(getClass().getResource(VistaController.LOGIN));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene;
                final String osName = System.getProperty("os.name");
                if (osName != null && osName.startsWith("Windows")) {
                    scene = (new LauncherPreloader.ShadowScene()).getShadowScene(root);
                    stage.initStyle(StageStyle.TRANSPARENT);
                } else {
                    assert root != null;
                    scene = new Scene(root);
                    stage.initStyle(StageStyle.UNDECORATED);
                }
                stage.setScene(scene);
                LoginWindowController loginWindowController = loader.getController();
                loginWindowController.setClient(client);
                VistaController.loadVista(VistaController.SignIn, loginWindowController);
                stage.show();
                WindowMoveHelper.addMoveListener(stage);
            }
        });
    }
}
