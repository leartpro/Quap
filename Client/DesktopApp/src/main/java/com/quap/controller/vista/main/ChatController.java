package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.Message;
import com.quap.client.domain.UserContent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class ChatController extends MainVistaNavigator{
    private Client client;

    @FXML
    private ScrollPane chatPane = new ScrollPane();

    @FXML
    private TextField textConsole = new TextField();

    @FXML
    private TextArea chatArea = new TextArea();
    private String type;

    @FXML
    public void initialize() {

    }

    @FXML
    public void send(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            if(textConsole.getText().length() > 0) {
                client.sendMessage(textConsole.getText());
            }
            textConsole.setText("");
        }
        //TODO: box shake event
    }

    @Override
    public void loadContent(List<UserContent> content) {
        for (Object o : content) {
            chatArea.appendText(o + "\n");
        }
    }

    @Override
    public void setType(String id) {
        this.type = id;
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    public void addMessage(Message message) {
        chatArea.appendText(message + "\n");
    }
}
