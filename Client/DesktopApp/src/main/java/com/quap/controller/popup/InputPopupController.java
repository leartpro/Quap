package com.quap.controller.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InputPopupController {
    @FXML
    private Button btnClose;

    @FXML
    private TextField userInput;
    private String type;

    @FXML
    void closePopup(ActionEvent actionEvent) {
        btnClose.setDisable(true);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    //gives to user request back to the client object
    @FXML
    void submit(ActionEvent event) {
        if(type.equals("chatrooms")) { //TODO:
            System.out.println("Füge Chatroom hinzu");
        } else if(type.equals("friends")) {
            System.out.println("Füge Friend hinzu");
        }
    }

    public void addType(String type) {
        this.type = type;
    }
}
