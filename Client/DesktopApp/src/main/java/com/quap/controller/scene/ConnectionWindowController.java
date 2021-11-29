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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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
        loadingLabel = lblLoading;
    }

    public ConnectionWindowController() {
        //TODO: start setupProjectThread as Future and let lauchWindow access later the returned data
        Thread loadingDummyThread = new Thread(() -> {
            StringBuilder text = new StringBuilder("Loading");
            for (int i = 0; i < 3; i++) {
                String finalText = text.toString();
                Platform.runLater(() -> loadingLabel.setText(finalText));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                text.append(".");
            }
        });
        loadingDummyThread.start();
        try {
            loadingDummyThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*Thread setupProjectStructure = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText("Create Project-File-System"));

            ConfigReader configReader = new ConfigReader("anonym");
            configReader.createUser();
            UserdataReader dataReader = new UserdataReader("anonym", "toor");

        });
        setupProjectStructure.start();
        try {
            setupProjectStructure.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public Callable<Boolean> connect() {
        return () -> {
            Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
            try {
                client = new Client("localhost", 0); //local socketAddress to bind to
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        };
    }

    public Callable<Boolean> openConnection() {
        return () -> {
            Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
            try {
                client.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try {
                client.setConnection();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        };
    }

    public Callable<Boolean> confirmConnection() {
        return () -> {
            System.out.println("confirm connection");
            Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
            client.listen();
            return true;
        };
    }

    public Callable<Boolean> launchWindow() {
        return () -> {
            Platform.runLater(() -> loadingLabel.setText("Loading Login"));
            Platform.runLater(task);
            return (Boolean) task.get();
        };
    }

    final FutureTask task = new FutureTask(new Callable<Boolean>() {
        @Override
        public Boolean call() {
            Stage stage = new Stage();
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(VistaController.LOGIN));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
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
            VistaController.setLoginWindowController(loginWindowController);
            VistaController.loadLoginVista(VistaController.SignIn);
            stage.show();
            WindowMoveHelper.addMoveListener(stage);
            return true;
        }
    });
}
