package com.quap.controller.scene;

import com.quap.client.Client;
import com.quap.client.domain.Chat;
import com.quap.client.domain.Friend;
import com.quap.client.domain.Message;
import com.quap.client.domain.UserContent;
import com.quap.client.utils.MainClientObserver;
import com.quap.controller.SceneController;
import com.quap.controller.VistaController;
import com.quap.controller.vista.MainVistaObserver;
import com.quap.controller.vista.VistaNavigator;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.quap.controller.VistaController.CHAT;

public class MainWindowController extends WindowController implements MainClientObserver, MainVistaObserver {

    private MainVistaNavigator currentNode;
    private double lastX = 0.0d, lastY = 0.0d, lastWidth = 0.0d, lastHeight = 0.0d;
    private ToggleGroup submenuGroup;
    public static Client client;
    public String currentNodeID;


    @FXML
    private VBox vBoxButtonHolder;
    @FXML
    private Label lblName;
    @FXML
    private ToggleButton btnFriends;
    @FXML
    private ToggleButton btnChatrooms;
    @FXML
    private ToggleButton btnProfil;
    @FXML
    private ToggleButton btnSettings;
    @FXML
    private StackPane stackContent;
    @FXML
    private Label lblServer_IP;

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
        ToggleGroup menuGroup = new ToggleGroup();
        submenuGroup = new ToggleGroup();
        btnFriends.setToggleGroup(menuGroup);
        btnChatrooms.setToggleGroup(menuGroup);
        btnProfil.setToggleGroup(menuGroup);
        btnSettings.setToggleGroup(menuGroup);

        menuGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                newValue.setSelected(true);
                if(oldValue != null) {
                    oldValue.setSelected(false);
                }
            }
        });
    }

    public void setVista(Parent node, VistaNavigator controller) { //set the current node is called by VistaController
        if (node.getId().equals("chat") || node.getId().equals("list")) {
            if(currentNode != null) {
                currentNode.removeObserver(this);
            }
            currentNodeID = node.getId();
            currentNode = (MainVistaNavigator)controller;
            currentNode.setClient(client);
            currentNode.addObserver(this);
            currentNode.setType("unknown");
        } else {
            throw new IllegalArgumentException();
        }
        stackContent.getChildren().setAll(node);
    }

    @FXML
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

    @FXML
    public void minimize(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        client.removeMainObserver(this);
        client.disconnect();
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    @FXML
    public void friends() {
        VistaController.loadVista(VistaController.LIST, this);
        currentNode.loadContent(new ArrayList<>(client.getFriends()));
        currentNode.setType("friends");
        loadButtons(client.getFriends());
    }

    public void loadButtons(List<UserContent> data) {
        vBoxButtonHolder.getChildren().clear();
        submenuGroup.getToggles().clear();
        for(UserContent content : data) {
            ToggleButton b = new ToggleButton(content.content());
            b.setOnAction(e -> {
                VistaController.loadVista(CHAT, this);
                currentNode.loadContent(
                        client.getMessagesByChat(content.chatID())
                );
                client.setCurrentChatID(content.chatID());
            });
            b.setToggleGroup(submenuGroup);
            b.setMinWidth(90);
            b.setMaxWidth(90);
            vBoxButtonHolder.getChildren().add(b);
        }
    }

    public void setName(String name) {
        lblName.setText(name);
    }

    @FXML
    public void chatrooms() {
        VistaController.loadVista(VistaController.LIST, this);
        currentNode.loadContent(new ArrayList<>(client.getChats()));
        currentNode.setType("chatrooms");
        loadButtons(client.getChats());
    }

    public void setClient(Client client) {
        MainWindowController.client = client;
        MainWindowController.client.addMainObserver(this);
        lblServer_IP.setText(lblServer_IP.getText() + " " + client.getConnectionInfo());

        //load the userdata into the default UI page
        friends();
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
            Platform.runLater(() -> loadButtons(client.getChats()));
            if(currentNodeID.equals("list")) {
                Platform.runLater(() -> currentNode.loadContent(Collections.singletonList(chat)));
            }
        }
    }

    @Override
    public void inviteEvent(Chat chat, int senderID, String senderName, List<String> participants) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/requestPopup.fxml"));
        Stage primaryStage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        List<String> info = new ArrayList<>();
        info.add("Invited by: " + senderName + "#" + senderID);
        Scanner scanner = new Scanner(chat.display());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            info.add(line);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("participants: " + "\n");
        for(String participant : participants) {
            sb.append(participant).append("\n");
        }
        info.add(sb.toString());
        scanner.close();
        Platform.runLater(() -> {
            boolean decision;
            decision = SceneController.submitRequestPopup(loader, primaryStage, info, "Your are invited to a chatroom");
            System.out.println("user decision:" + decision);
            if (decision) {
                System.out.println("send join-chat-request to the server with chat and sender_id...");
                client.joinChat(chat.chatID());
            }
        });
    }

    @Override
    public void joinChatEvent(Chat chat) {
        System.out.println("joinChatEvent");
        if(currentNode.getType().equals("chatrooms")) {
            Platform.runLater(() -> loadButtons(client.getChats()));
            if(currentNodeID.equals("list")) {
                Platform.runLater(() -> currentNode.loadContent(Collections.singletonList(chat)));
            }
        }
    }

    @Override
    public void friendRequestEvent(Friend friend) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/popup/requestPopup.fxml"));
        Stage primaryStage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        List<String> info = new ArrayList<>();
        info.add("Invited by: " + friend.name() + "#" + friend.id());
        Scanner scanner = new Scanner(friend.display());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            info.add(line);
        }
        scanner.close();
        Platform.runLater(() -> {
            boolean decision;
            decision = SceneController.submitRequestPopup(loader, primaryStage, info, "You have a friend invite");
            System.out.println("user decision:" + decision);
            if (decision) {
                client.acceptFriend(friend.id());
            }
        });
    }

    @Override
    public void addFriendEvent(Friend friend) {
        System.out.println("createChatEvent");
        System.out.println(currentNode.getType());
        if(currentNode.getType().equals("friends")) {
            Platform.runLater(() -> loadButtons(client.getFriends()));
            if(currentNodeID.equals("list")) {
                Platform.runLater(() -> currentNode.loadContent(Collections.singletonList(friend)));
            }
        }
    }

    @Override
    public void deleteChatEvent() {
        System.out.println("deleteChatEvent");
        if(currentNode.getType().equals("chatrooms")) {
            Platform.runLater(() -> loadButtons(client.getChats()));
            if(currentNodeID.equals("list")) {
                Platform.runLater(() -> currentNode.loadContent(new ArrayList<>(client.getChats())));
            }
        }
    }

    @Override
    public void unfriendEvent() {
        System.out.println("unfriendEvent");
        if(currentNode.getType().equals("friends")) {
            Platform.runLater(() -> loadButtons(client.getFriends()));
            if(currentNodeID.equals("list")) {
                Platform.runLater(() -> currentNode.loadContent(new ArrayList<>(client.getFriends())));
            }
        }
    }
}
