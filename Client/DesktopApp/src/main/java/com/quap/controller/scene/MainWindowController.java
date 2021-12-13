package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.client.domain.Chat;
import com.quap.client.domain.Friend;
import com.quap.client.domain.Message;
import com.quap.client.domain.UserContent;
import com.quap.client.utils.ClientObserver;
import com.quap.controller.SceneController;
import com.quap.controller.VistaController;
import com.quap.controller.vista.main.ChatController;
import com.quap.controller.vista.main.MainVistaNavigator;
import javafx.application.Platform;
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
import java.util.Collections;

import static com.quap.controller.VistaController.CHAT;

public class MainWindowController implements ClientObserver {

    private MainVistaNavigator currentNode;
    private double lastX = 0.0d, lastY = 0.0d, lastWidth = 0.0d, lastHeight = 0.0d;

    @FXML
    private StackPane stackContent;
    @FXML
    private VBox vBoxButtonHolder;
    @FXML
    private Label lblServer_IP;

    public static Client client;
    public String currentNodeID;

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
            currentNodeID = node.getId();
            currentNode = controller;
            currentNode.setClient(client);
        } else {
            throw new IllegalArgumentException();
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
        //TODO: close the window way faster
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
        currentNode.setType("friends");
        vBoxButtonHolder.getChildren().clear();

        for (UserContent friend : client.getFriends()) {
            Button b = new Button(((Friend) friend).name());
            b.setOnAction(e -> {
                VistaController.loadMainVista(CHAT);
                currentNode.loadContent(
                        client.getMessagesByChat(((Friend) friend).chatID())
                );
                client.setCurrentChatID(((Friend) friend).chatID());
            });
            vBoxButtonHolder.getChildren().add(b);
        }
    }

    public void chatrooms(ActionEvent actionEvent) {
        VistaController.loadMainVista(VistaController.LIST);
        //load the userdata into the default UI page
        currentNode.loadContent(client.getChats());
        currentNode.setType("chatrooms");
        vBoxButtonHolder.getChildren().clear();

        for (UserContent chat : client.getChats()) {
            Button b = new Button(((Chat) chat).name());
            b.setOnAction(e -> {
                VistaController.loadMainVista(CHAT);
                currentNode.loadContent(
                        client.getMessagesByChat(((Chat) chat).id())
                );
                client.setCurrentChatID(((Chat) chat).id());
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
        MainWindowController.client.addObserver(this);
        lblServer_IP.setText(lblServer_IP.getText() + " " + client.getConnectionInfo());

        //load the userdata into the default UI page
        friends(null);
    }

    @Override
    public void messageEvent(Message message) {
        System.out.println("messageEvent");
        if (currentNodeID.equals("chat")) {
            ((ChatController) currentNode).addMessage(message);
        }
    }

    @Override
    public void createChatEvent(Chat chat) {
        System.out.println("createChatEvent");
        if(currentNode.getType().equals("chatrooms")) {
            Platform.runLater(() -> {
                vBoxButtonHolder.getChildren().clear();
                for (UserContent content : client.getChats()) {
                    Button b = new Button(((Chat) content).name());
                    b.setOnAction(e -> {
                        VistaController.loadMainVista(CHAT);
                        currentNode.loadContent(
                                client.getMessagesByChat(((Chat) content).id())
                        );
                        client.setCurrentChatID(((Chat) content).id());
                    });
                    vBoxButtonHolder.getChildren().add(b);
                }
            });
            if(currentNodeID.equals("list")) {
                Platform.runLater(() -> {
                    currentNode.loadContent(Collections.singletonList(chat));
                });
            }
        }
    }

    @Override
    public void inviteEvent(Chat chat, int senderID) { //TODO: submit requestPopup if the user wants to accept or decline
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/requestPopup.fxml"));
        Stage primaryStage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        String message = "invited by: " + "sample user" + "to the chat: " + chat;
        Platform.runLater(() -> {
            boolean decision;
            decision = SceneController.submitRequestPopup(loader, primaryStage, message);
            System.out.println("user decision:" + decision);
            if (decision) {
                //TODO: send join-chat-request to the server with chat and sender_id
                System.out.println("send join-chat-request to the server with chat and sender_id...");

            } else {
                client.removeLokalChat(chat);
            }
        });
    }
}
