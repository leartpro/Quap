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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

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
        //###########################
        Thread loadingDummyThread = new Thread(() -> {
            String text = "Loading";
            for(int i = 0; i < 3; i++) {
                String finalText = text;
                Platform.runLater(() -> loadingLabel.setText(finalText));
                try { Thread.sleep(1000);} catch (InterruptedException e) {
                    e.printStackTrace(); }
                text = text + ".";
            }});
        loadingDummyThread.start();
        try { loadingDummyThread.join(); } catch (InterruptedException e) {
            e.printStackTrace(); }
        //###########################

        Thread setupProjectStructure = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText("Create File-System"));
            String rootPath = "./Client/src/main/resources/com/quap/users/anonym/";
            boolean fileExisting = false;
            boolean folderExisting = false;
            File sqlFolder, prefFolder;
            File sqlFile, prefFile;

            sqlFolder = new File(rootPath + "/sqlite/db/");
            sqlFolder.mkdirs();
            sqlFile = new File(rootPath + "/sqlite/db/" + "messages.db");
            try {
                sqlFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            prefFolder = new File(rootPath + "/preferences/settings/");
            prefFolder.mkdirs();
            prefFile = new File(rootPath + "/preferences/settings/" + "settings.properties");
            try {
                prefFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (sqlFolder.exists() && prefFolder.exists()) {
                folderExisting = true;
            }
            if (sqlFile.exists() && prefFile.exists()) {
                fileExisting = true;
            }
            System.out.println(folderExisting && fileExisting);



        }); setupProjectStructure.start();
        try {
            setupProjectStructure.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AtomicBoolean success = new AtomicBoolean(false);
        Thread openConnection = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
                client = new Client("localhost", 80); //local socketaddress to bind to
                success.set(client.openConnection()); //TODO: run as future the db and file system creation
                client.setConnection(); //does this when openConnection is finished
                client.listen();
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

                    loginWindowController.setClient(client);
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
