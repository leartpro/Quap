package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.client.domain.Chat;
import com.quap.client.domain.Friend;
import com.quap.client.domain.UserContent;
import com.quap.controller.VistaController;
import com.quap.controller.vista.main.MainVistaNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

import static com.quap.controller.VistaController.CHAT;

public class MainWindowController {

    private MainVistaNavigator currentNode;
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
        Parent node;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(VistaController.class.getResource(VistaController.LIST));
            node = loader.load();
            setVista(node, loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setVista(Node node, MainVistaNavigator controller) { //set the current node is called by VistaController
        if (node.getId().equals("chat") || node.getId().equals("list") || node.getId().equals("profile") || node.getId().equals("settings")) {
            currentNode = controller;
            currentNode.setClient(client);
        } else {
            IllegalArgumentException e;
        }
        stackContent.getChildren().setAll(node);
    }

    public void maximize(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Window w = n.getScene().getWindow();
        double currentX = w.getX(), currentY = w.getY(), currentWidth = w.getWidth(), currentHeight = w.getHeight();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        if (currentX != bounds.getMinX() &&
                currentY != bounds.getMinY() &&
                currentWidth != bounds.getWidth() &&
                currentHeight != bounds.getHeight()) {
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
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void close(ActionEvent actionEvent) {
        ((Button) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void settings(ActionEvent actionEvent) {
        /*VistaController.loadMainVista(VistaController.SETTINGS);
        vBoxButtonHolder.getChildren().clear();
        for (Button b : new ArrayList<>(List.of(new Button[]{new Button("setting1"), new Button("setting2"),
                new Button("setting3"), new Button("setting4")}))) {
            b.setOnAction(e -> {
                VistaController.loadMainVista(CHAT); //TODO: load setting specific
                currentNode.loadContent("Theme: dark", "Privacy: disable"); //dummys
            });
            vBoxButtonHolder.getChildren().add(b);
        }*/
    }

    public void friends(ActionEvent actionEvent) {
        VistaController.loadMainVista(VistaController.LIST);
        //load the userdata into the default UI page
        currentNode.loadContent(client.getFriends());
        vBoxButtonHolder.getChildren().clear();

        for(UserContent friend : client.getFriends()) {
            Button b = new Button(((Friend)friend).name());
            b.setOnAction(e -> {
                VistaController.loadMainVista(CHAT);
                currentNode.loadContent(
                        client.getMessagesByChat(((Friend)friend).chatID())
                );
            });
            vBoxButtonHolder.getChildren().add(b);
        }
    }

    public void chatrooms(ActionEvent actionEvent) {
        VistaController.loadMainVista(VistaController.LIST);
        //load the userdata into the default UI page
        currentNode.loadContent(client.getChats());
        vBoxButtonHolder.getChildren().clear();

        for(UserContent chat : client.getChats()) {
            Button b = new Button(((Chat)chat).name());
            b.setOnAction(e -> {
                VistaController.loadMainVista(CHAT);
                currentNode.loadContent(
                        client.getMessagesByChat(((Chat)chat).id())
                );
            });
            vBoxButtonHolder.getChildren().add(b);
        }
    }

    public void profil(ActionEvent actionEvent) {
        /*VistaController.loadMainVista(VistaController.PROFILE);
        vBoxButtonHolder.getChildren().clear();
        for (Button b : new ArrayList<Button>(List.of(new Button[]{new Button("prSetting1"), new Button("prSetting2"),
                new Button("prSetting3"), new Button("prSetting4")}))) {
            b.setOnAction(e -> {
                VistaController.loadMainVista(CHAT);//TODO: load profile-setting specific
                currentNode.loadContent("Name: User", "Password: *****"); //dummys
            });
            vBoxButtonHolder.getChildren().add(b);
        }*/
    }

    public void setClient(Client client) {
        MainWindowController.client = client;
        lblServer_IP.setText(lblServer_IP.getText() + " " + client.getConnectionInfo());

        //load the userdata into the default UI page
        currentNode.loadContent(client.getFriends());
        vBoxButtonHolder.getChildren().clear();

        for(UserContent friend : client.getFriends()) {
            Button b = new Button(((Friend)friend).name());
            b.setOnAction(e -> {
                VistaController.loadMainVista(CHAT);
                currentNode.loadContent(
                        client.getMessagesByChat(((Friend)friend).id())
                );
            });
            vBoxButtonHolder.getChildren().add(b);
        }
    }
}
