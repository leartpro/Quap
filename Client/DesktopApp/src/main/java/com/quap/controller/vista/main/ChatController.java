package com.quap.controller.vista.main;

import com.quap.controller.scene.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class ChatController extends MainVistaNavigator{

    @FXML
    private TextArea chatPane = new TextArea();

    @FXML
    private TextField textConsole;

    @FXML
    public void send(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            MainWindowController.client.sendMessage(textConsole.getText());
        }
    }

    @Override
    public void loadContent(List content) {
        for (Object o : content) {
            chatPane.setText(o.toString());
        }
    }
}
