package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.client.data.ConfigReader;
import com.quap.client.utils.LoginClientObserver;
import com.quap.controller.SceneController;
import com.quap.controller.VistaController;
import com.quap.controller.vista.LoginVistaObserver;
import com.quap.controller.vista.VistaNavigator;
import com.quap.controller.vista.login.LoginVistaNavigator;
import com.quap.utils.ResizeHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Diese KLasse behandelt die Nutzerinteraktion im Login Fenster
 */
public class LoginWindowController extends WindowController implements LoginVistaObserver, LoginClientObserver {
    private LoginVistaNavigator currentNode;
    private Client client;
    private boolean existingUser;
    private String name;

    public void setClient(Client client) {
        this.client = client;
        client.addLoginObserver(this);
    }

    public void setVista(Parent node, VistaNavigator controller) {
        if (node.getId().equals("signUp") || node.getId().equals("signIn")) {
            currentNode = (LoginVistaNavigator) controller;
        } else {
            throw new IllegalArgumentException();
        }
        currentNode.addObserver(this);
        vistaHolder.getChildren().setAll(node);
        if (node.getId().equals("signUp")) {
            existingUser = false;
        } else if (node.getId().equals("signIn")) {
            existingUser = true;
        } else {
            System.err.println("Unknown node");
        }
    }

    @Override
    public void swapVistaEvent(String vista) {
        VistaController.loadVista(vista, this);
    }

    @Override
    public void toggleLoginEvent(boolean isValid) {
        btnLogin.setVisible(isValid);
    }

    @FXML
    private StackPane vistaHolder;

    @FXML
    private CheckBox checkAnonymMode;

    @FXML
    private final Button btnLogin = new Button();

    @FXML
    public void initialize() {
        checkAnonymMode.setSelected(false);
        btnLogin.setVisible(false);
        VistaController.loadVista(VistaController.SignUp, this);
    }

    @FXML
    void switchMode() {
        if (checkAnonymMode.isSelected()) {
            currentNode.switchMode(true);
            btnLogin.setVisible(true);
        } else {
            currentNode.switchMode(false);
            btnLogin.setVisible(currentNode.validLogin());
        }
    }

    @FXML
    public void login() {
        btnLogin.setVisible(true);
        name = currentNode.getName();
        String password = currentNode.getPassword();
        client.authorize(name, password, existingUser);
    }

    @Override
    public void authFailedEvent(String error) {
        System.out.println("authFailedEvent");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/popup.fxml"));
            Stage primaryStage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
            Platform.runLater(() -> SceneController.submitPopup(loader, primaryStage, error, "Login"));
            btnLogin.setDisable(false);
    }

    /**
     * Diese Methode wird aufgerufen, wenn der Server zurückgegeben hat, dass Nutzername und Passwort übereinstimmen.
     * Bei einem Methodenaufruf wird das Hauptfenster geladen.
     */
    @Override
    public void authSuccessEvent() {
        System.out.println("authSuccessEvent");
        ConfigReader configReader = new ConfigReader(name);
        if (!existingUser) {
            configReader.init();
        } else {
            configReader.validateUser();
        }
        client.connectDB();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/scene/main-window.fxml"));
                Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert root != null;
                Scene scene = new Scene(root);
                assert stage != null;
                stage.setScene(scene);
                stage.setMinWidth(600);
                stage.setMinHeight(400);
                stage.setWidth(1080);
                stage.setHeight(720);
                MainWindowController mainWindowController = loader.getController();
                mainWindowController.setClient(client);
                mainWindowController.setName(name);
                stage.show();
                ResizeHelper.addResizeListener(stage);
                stage.show();
            }
        });
    }
}
