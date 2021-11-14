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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
    The loading screen of this application
 */
public class ConnectionWindowController implements Initializable {
    private Client client;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final List<Map<Boolean, Future<Boolean>>> resultList = new ArrayList<>();


    @FXML
    Label lblLoading;
    private static Label loadingLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingLabel= lblLoading;
    }

    public ConnectionWindowController() {
        /*
        try {
            resultList.add((Map<Boolean, Future<Boolean>>) new HashMap<Boolean, Future<Boolean>>()
                    .put(true,
                            executor.submit(new ConfigReader("")
                            )
                    )
            );
        } catch (IOException | InvalidPreferencesFormatException e) {
            e.printStackTrace();
        }
        for(Map<Boolean, Future<Boolean>> pair : resultList) {
            Optional<Boolean> optional = pair.keySet().stream().findFirst();
            if(optional.isPresent()) {
                var key = optional.get();

                System.out.printf("Value is: %d%n", key);

                var future = pair.get(key);
                Boolean result = null;
                try {
                    result = future.get(10, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
                var isDone = future.isDone();

                System.out.printf("Result is %d%n", result);
                System.out.printf("Task done: %b%n", isDone);
                System.out.println("--------------------");
            }
        } executor.shutdown();
         */

        //TODO: start setupProjectThread as Future and let lauchWindow access later the returned data
        Thread loadingDummyThread = new Thread(() -> {
            StringBuilder text = new StringBuilder("Loading");
            for(int i = 0; i < 3; i++) {
                String finalText = text.toString();
                Platform.runLater(() -> loadingLabel.setText(finalText));
                try { Thread.sleep(1000);} catch (InterruptedException e) { e.printStackTrace(); }
                text.append("."); }
        }); loadingDummyThread.start();
        try { loadingDummyThread.join(); } catch (InterruptedException e) { e.printStackTrace(); }


        Thread setupProjectStructure = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText("Create File-System"));
            String rootPath = "./Client/src/main/resources/com/quap/users/anonym/";
            File sqlFolder, prefFolder;
            File prefFile;
            sqlFolder = new File(rootPath + "/sqlite/db/");
            sqlFolder.mkdirs();
            prefFolder = new File(rootPath + "/preferences/settings/");
            prefFolder.mkdirs();
            prefFile = new File(rootPath + "/preferences/settings/" + "settings.properties");
            try {
                prefFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }); setupProjectStructure.start();
        try {
            setupProjectStructure.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        Thread connect = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
                client = new Client("localhost", 80); //local socketAddress to bind to
        }); connect.start();
        try {
            connect.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void openConnection() {
        Thread openConnection = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
            client.openConnection();
            client.setConnection();
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

    }

    public void confirmConnection() {
        Thread confirmConnection = new Thread(() -> {
            Platform.runLater(() -> loadingLabel.setText("Open Connection..."));
            client.listen();
        });
        confirmConnection.start();
        try {
            confirmConnection.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void launchWindow() {
        Thread openLoginThread = new Thread(() -> {
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
                    if( osName != null && osName.startsWith("Windows") ) {
                        scene = (new LauncherPreloader.ShadowScene()).getShadowScene(root);
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
        });
        openLoginThread.start();
        try {
            openLoginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
