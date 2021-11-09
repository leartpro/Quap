package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.controller.VistaController;
import com.quap.controller.vista.main.MainVistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainWindowController {

    private MainVistaNavigator currentNode;
    private final Vista vista = new Vista();
    private double lastX = 0.0d, lastY = 0.0d, lastWidth = 0.0d, lastHeight = 0.0d;

    @FXML
    private StackPane stackContent;
    @FXML
    private VBox vBoxButtonHolder;
    @FXML
    private Label lblServer_IP;
    public static Client client;

    @FXML
    public void initialize() {
        currentNode = (MainVistaNavigator) vista.getVistaByID("profile");
        //TODO: start as future, waiting until the info is received
        //lblServer_IP.setText(lblServer_IP.getText() + " " + client.getConnectionInfo());
    }

    public void maximize(ActionEvent actionEvent) {
        Node n = (Node)actionEvent.getSource();
        Window w = n.getScene().getWindow();
        double currentX = w.getX(), currentY = w.getY(), currentWidth = w.getWidth(), currentHeight = w.getHeight();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        if( currentX != bounds.getMinX() &&
                currentY != bounds.getMinY() &&
                currentWidth != bounds.getWidth() &&
                currentHeight != bounds.getHeight() ) {
            w.setX(bounds.getMinX());
            w.setY(bounds.getMinY());
            w.setWidth(bounds.getWidth());
            w.setHeight(bounds.getHeight());
            lastX = currentX;  // save old dimensions
            lastY = currentY;
            lastWidth = currentWidth;
            lastHeight = currentHeight;
        } else { // de-maximize the window (not same as minimize)
            w.setX(lastX);
            w.setY(lastY);
            w.setWidth(lastWidth);
            w.setHeight(lastHeight);
        }
        actionEvent.consume();  // don't bubble up to title bar
    }

    public void minimize(ActionEvent actionEvent) {
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void close(ActionEvent actionEvent) {
        ((Button)actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void setVista(Node node) { //set the current node is called by VistaControler
        if(node.getId().equals("chat") || node.getId().equals("list") || node.getId().equals("profile") || node.getId().equals("settings")) {
            currentNode = (MainVistaNavigator) vista.getVistaByID(node.getId()); //currentNode.setRootNode(this);
        } else {
            IllegalArgumentException e;
        }
        stackContent.getChildren().setAll(node);
    }

    public void settings(ActionEvent actionEvent) { //?????????????????
        VistaController.loadMainVista(VistaController.SETTINGS); //calls indirect setVista
        vBoxButtonHolder.getChildren().clear();
        for(Button b : new ArrayList<>(List.of(new Button[]{new Button("setting1"), new Button("setting2"),
                new Button("setting3"), new Button("setting4")}))) {
            vBoxButtonHolder.getChildren().add(b);
        }
    }

    public void friends(ActionEvent actionEvent) {
        VistaController.loadMainVista(VistaController.LIST);
        vBoxButtonHolder.getChildren().clear();
        for(Button b : new ArrayList<Button>(List.of(new Button[]{new Button("friend1"), new Button("friend2"),
                new Button("friend3"), new Button("friend4")}))) {
            b.setOnAction(e -> {
                VistaController.loadMainVista(VistaController.CHAT);
                //TODO get vista controller
                //((ChatController) vista.getVistaByID("chat")).loadContent(Collections.singletonList("Test Content"));
                currentNode.loadContent(Collections.singletonList("Test Content"));
            });
            vBoxButtonHolder.getChildren().add(b);
        }
    }

    public void chatrooms(ActionEvent actionEvent) {
        VistaController.loadMainVista(VistaController.LIST);
        vBoxButtonHolder.getChildren().clear();
        for(Button b : new ArrayList<Button>(List.of(new Button[]{new Button("chatroom1"), new Button("chatroom2"),
                new Button("chatroom3"), new Button("chatroom4")}))) {
            vBoxButtonHolder.getChildren().add(b);
        }
    }

    public void profil(ActionEvent actionEvent) {
        VistaController.loadMainVista(VistaController.PROFILE);
        vBoxButtonHolder.getChildren().clear();
        for(Button b : new ArrayList<Button>(List.of(new Button[]{new Button("prSetting1"), new Button("prSetting2"),
                new Button("prSetting3"), new Button("prSetting4")}))) {
            vBoxButtonHolder.getChildren().add(b);
        }
    }

    public void setClient(Client client) {
        MainWindowController.client = client;
    }

    private class Vista extends MainVistaNavigator { //?????????????????????
        //vista functions

        @Override
        public void loadContent(List content) {

        }
    }
    }
