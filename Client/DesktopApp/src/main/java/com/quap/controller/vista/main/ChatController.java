package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.Content;
import com.quap.client.domain.Message;
import com.quap.controller.vista.MainVistaObserver;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatController extends MainVistaNavigator{
    private Client client;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<MainVistaObserver> observers = new ArrayList<>();

    @FXML
    private ScrollPane chatPane = new ScrollPane();

    @FXML
    private TextField textConsole = new TextField();

    @FXML
    private TextArea chatArea = new TextArea();
    private String type;

    @FXML
    public void send(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            if(textConsole.getText().length() > 0) {
                client.sendMessage(textConsole.getText());
            }
            textConsole.setText("");
        }
    }

    @Override
    public void loadContent(List<Content> content) {
        for (Content o : content) {
            chatArea.appendText(o.content() + "\n");
        }
    }

    @Override
    public void setType(String id) {
        this.type = id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void addObserver(MainVistaObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MainVistaObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    public void addMessage(Message message) {
        chatArea.appendText(message.content() + "\n");
    }
}
