package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.client.data.ConfigReader;
import com.quap.controller.VistaController;
import com.quap.controller.vista.LoginVistaObserver;
import com.quap.controller.vista.VistaNavigator;
import com.quap.controller.vista.login.LoginVistaNavigator;
import com.quap.utils.ResizeHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginWindowController extends WindowController implements LoginVistaObserver {
    private LoginVistaNavigator currentNode;
    private Client client;
    private boolean existingUser;

    public void setClient(Client client) {
        this.client = client;
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
    private Button btnLogin = new Button();

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
    public void login(ActionEvent actionEvent) {
        String name = currentNode.getName();
        String password = currentNode.getPassword();

        ConfigReader configReader = new ConfigReader(name);
        if (!existingUser) {
            configReader.init();
        } else {
            configReader.validateUser();
        }
        client.authorize(name, password, existingUser);
        client.connectDB();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/scene/main-window.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert root != null;
                Scene scene = new Scene(root);
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
