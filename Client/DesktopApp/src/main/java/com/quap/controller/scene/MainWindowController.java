package com.quap.controller.scene;

import com.quap.controller.vista.VistaNavigator;
import com.quap.controller.vista.login.LoginVistaNavigator;
import com.quap.controller.vista.main.MainVistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainWindowController {
    private MainVistaNavigator currentNode;
    private final Vista vista = new Vista();

    @FXML
    private StackPane stackContent;

    @FXML
    private BorderPane paneSidebar;

    @FXML
    private VBox vBoxButtonHolder;

    @FXML
    private Label lblServer_IP;


    public void setVista(Object load) {
    }

    public void maximize(ActionEvent actionEvent) {
    }

    public void minimize(ActionEvent actionEvent) {
    }

    public void close(ActionEvent actionEvent) {
    }

    private class Vista extends LoginVistaNavigator {

        @Override
        public boolean validLogin() {
            return false;
        }

        @Override
        public void switchMode(boolean isSelected) {

        }

        public VistaNavigator getVistaByID(String ID) {
            return super.getVistaByID(ID);
        }
    }

    @FXML
    public void initialize() {
        currentNode = (MainVistaNavigator) vista.getVistaByID("profile");
    }
    }
