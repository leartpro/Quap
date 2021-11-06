package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.controller.VistaController;
import com.quap.desktopapp.LauncherPreloader;
import com.quap.listener.WindowMoveHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    Label lblLoading;
    private static Label loadingLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingLabel= lblLoading;
    }

    public void connect() {

        /*ProgressIndicator pi = new ProgressIndicator();
        Task<Void> counter = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                for (int i = 1; i <= 100; i++) {
                    Thread.sleep(50);
                    updateProgress(i, 100);
                }
                return null;
            }
        };

        pi.progressProperty().bind(counter.progressProperty());
        pi.progressProperty().addListener((obs, oldProgress, newProgress) -> {
            PseudoClass warning = PseudoClass.getPseudoClass("warning");
            PseudoClass critical = PseudoClass.getPseudoClass("critical");
            if (newProgress.doubleValue() < 0.3) {
                pi.pseudoClassStateChanged(warning, false);
                pi.pseudoClassStateChanged(critical, true);
            } else if (newProgress.doubleValue() < 0.65) {
                pi.pseudoClassStateChanged(warning, true);
                pi.pseudoClassStateChanged(critical, false);
            } else {
                pi.pseudoClassStateChanged(warning, false);
                pi.pseudoClassStateChanged(critical, false);
            }
        });*/


        Thread loadingDummyThread = new Thread(() -> {
            //Platform.runLater(() -> loadingLabel.setText(information[4]));
            String text = "Loading";
            for(int i = 0; i < 3; i++) {
                String finalText = text;
                Platform.runLater(() -> loadingLabel.setText(finalText));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                text = text + ".";
            }
        });
        loadingDummyThread.start();
        try {
            loadingDummyThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //#####################
        Thread openConnection = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
                client = new Client("localhost", 8080);
                boolean success = client.openConnection();
                if(success) {
                    client.authorize();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        });
        openConnection.start();
        try {
            openConnection.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //#####################
        

        Thread openLoginThread = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                    String osName = System.getProperty("os.name");
                    if( osName != null && osName.startsWith("Windows") ) {
                        scene = (new LauncherPreloader.WindowsHack()).getShadowScene(root);
                        stage.initStyle(StageStyle.TRANSPARENT);
                    } else {
                        scene = new Scene(root);
                        stage.initStyle(StageStyle.UNDECORATED);
                    }
                    stage.setScene(scene);
                    LoginWindowController loginWindowController = loader.getController();

                    VistaController.setLoginWindowController(loginWindowController);
                    VistaController.loadLoginVista(VistaController.SignIn);
                    stage.show();
                    WindowMoveHelper.addMoveListener(stage);
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        openLoginThread.start();
        try {
            openLoginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void openConnection() {
    }

    public void confirmConnection() {
    }
}
