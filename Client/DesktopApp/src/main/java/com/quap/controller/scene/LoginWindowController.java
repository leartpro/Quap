package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.client.data.ConfigReader;
import com.quap.controller.VistaController;
import com.quap.controller.vista.VistaNavigator;
import com.quap.controller.vista.login.LoginVistaNavigator;
import com.quap.utils.ResizeHelper;
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

public class LoginWindowController {
    private String name, password;
    private final Vista vista = new Vista();
    private LoginVistaNavigator currentNode;
    private Client client;
    private boolean existingUser;

    public void setClient(Client client) {
        this.client = client;
        //TODO: name should be the current name from the UI!!!
        /*this.name = "exampleName";
        this.password = "example!50AB";*/
    }

    public void setVista(Parent node, LoginVistaNavigator controller) {
        if(node.getId().equals("signUp") || node.getId().equals("signIn")) {
            currentNode = controller;
        } else {
            IllegalArgumentException e;
        }
        vistaHolder.getChildren().setAll(node);
        if(node.getId().equals("signUp")) {
            existingUser = false;
        } else if(node.getId().equals("signIn")) {
            existingUser = true;
        } else {
            System.err.println("Unknown node");
        }
    }

    private class Vista extends LoginVistaNavigator {
        @Override
        public boolean validLogin() {
            return false;
        }
        @Override
        public void switchMode(boolean isSelected) {

        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        public VistaNavigator getVistaByID(String ID) {
            return super.getVistaByID(ID);
        }
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
        currentNode = (LoginVistaNavigator) vista.getVistaByID("signUp");
        System.out.println(currentNode.getClass().getSimpleName());
        System.out.println("ID:" + currentNode.getId());
    }


    //TODO: fill methods
    @FXML
    void switchMode(ActionEvent event) {
        if(checkAnonymMode.isSelected()) {
            currentNode.switchMode(true);
            btnLogin.setVisible(true);
        } else {
            currentNode.switchMode(false);
            btnLogin.setVisible(currentNode.validLogin());
        }
    }

    public void toggleLogin(boolean isValid) {
        btnLogin.setVisible(isValid);
    }

    public void login(ActionEvent actionEvent) {
        //TODO: run as future the server request and in addition to the db connection and property reading
        name = currentNode.getName();
        password = currentNode.getPassword();
        client.authorize(name, password, existingUser);
        //if authentication is successful:
        ConfigReader configReader = new ConfigReader(name);
        if(existingUser) {
            configReader.readUser();
        } else {
            configReader.createUser();
            configReader.readUser(); //TODO define readUser()
        }
        //Config configuration = configReader.readConfiguration();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/scene/main-window.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
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
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setClient(client);
        //mainWindowController.setConfiguration(configuration);
        //TODO: give attributes to main scene controller
        VistaController.setMainWindowController(mainWindowController);
        //VistaController.loadMainVista(VistaController.LIST); //TODO: necessary?
        //TODO: receive future result and validate
        stage.show();
        ResizeHelper.addResizeListener(stage);
        stage.show();
    }
}
