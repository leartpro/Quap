package com.quap.controller.vista.main;

import com.quap.controller.scene.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ChatController extends MainVistaNavigator{

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
            MainWindowController.client.sendMessage(textConsole.getText());
        }
    }


    @Override
    public void loadContent(Object... content) {
        for (Object o : content) {
            chatArea.appendText(o + "\n");
        }
    }
}
