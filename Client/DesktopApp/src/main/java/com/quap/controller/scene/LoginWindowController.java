package com.quap.controller.scene;

import com.quap.controller.vista.VistaNavigator;
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

/*
    The login window of this application
 */
public class LoginWindowController {
    private String name, password;
    private VistaNavigator currentNode;

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
        currentNode = VistaNavigator.getVistaByID("signUp");
        //currentNode.setRootNode(this);
    }

    public void setVista(Node node) {
        if(node.getId().equals("signUp") || node.getId().equals("signIn")) {
            currentNode = VistaNavigator.getVistaByID(node.getId());
            //currentNode.setRootNode(this);
        } else {
            IllegalArgumentException e;
        }
        vistaHolder.getChildren().setAll(node);
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
        //confirm username with server
        //check for lokal profil
        //load main
    }

    public void loadMain(ActionEvent e) throws IOException {
        //TODO: give attributes to main scene controller
        //TODO: set size (not resizable???)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/scene/main-window.fxml"));
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
