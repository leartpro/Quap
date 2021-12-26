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

/*
    The loading screen of this application
 */
public class ConnectionWindowController implements Initializable {
    private Client client;

    @FXML
    private Label lblLoading;

    @FXML
    private ProgressBar progressBar;

    private static Label loadingLabel;
    private static ProgressBar prBar;
    private static final double EPSILON = 0.0000005;
    double progress;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("init");
        loadingLabel = lblLoading;
        prBar = progressBar;
        /*progressBar.progressProperty().bind(
                new Task<>() {
                    final int N_ITERATIONS = 100;
                    @Override
                    protected Void call() throws Exception {
                        for (int i = 0; i < N_ITERATIONS; i++) {
                            updateProgress(i + 1, N_ITERATIONS);
                            Thread.sleep(10);
                        }
                        return null;
                    }
                }.progressProperty()
        );*/
        /*prBar.progressProperty().bind(new Task<>() {
                    final int N_ITERATIONS = 100;
                    @Override
                    protected Object call() throws InterruptedException {
                        for (int i = 0; i < N_ITERATIONS; i++) {
                            updateProgress(i + 1, N_ITERATIONS);
                            Thread.sleep(500);
                        }
                        return null;
                    }
                }.progressProperty()
        );
        prBar.progressProperty().addListener(observable -> {
            if (prBar.getProgress() >= 1 - EPSILON) {
                prBar.setStyle("-fx-accent: forestgreen;");
            }
        });*/
    }

    public void increaseProgress() {
        if (progress < 1) {
            for(int i = 0; i < 25; i++) {
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

    public void connect() {
        increaseProgress();
        Platform.runLater(() -> loadingLabel.setText("Open Connection"));
        try {
            client = new Client("localhost", 0); //local socketAddress to bind to
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public void openConnection() {
        increaseProgress();
        Platform.runLater(() -> loadingLabel.setText("Open Connection."));
        try {
            client.openConnection();
        } catch (IOException e) {
            System.out.println("Error");
        }
        Platform.runLater(() -> loadingLabel.setText("Open Connection.."));
        try {
            client.setConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
    }

    public void confirmConnection() {
        increaseProgress();
        System.out.println("confirm connection");
        Platform.runLater(() -> loadingLabel.setText("Confirm Connection"));
        client.listen();
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
