package com.quap.controller.vista.main;

import com.quap.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ChatController extends MainVistaNavigator{
    private Client client;

    @FXML
    private ScrollPane chatPane = new ScrollPane();

    @FXML
    private TextField textConsole = new TextField();

    @FXML
    private TextArea chatArea = new TextArea();

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
    public void loadContent(Object... content) {
        for (Object o : content) {
            chatArea.appendText(o + "\n");
        }
    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }
}
