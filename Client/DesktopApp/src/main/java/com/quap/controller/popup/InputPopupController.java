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
        if(type.equals("chatrooms")) {
            System.out.println("Füge Chatroom hinzu");
            //TODO: erstelle den chatroom anfrage zum server
            // Server Sendet chatroom zurück und UI wird über observer updated
            // add a boolean to the participant table, wichs shows, if the current user has already joined or is just invited
        } else if(type.equals("friends")) {
            System.out.println("Füge Friend hinzu");
            //TODO: erstelle den chatroom anfrage zum server
            // Server Sendet chatroom zurück und UI wird über observer updated
            // check if there if only one entrance(just request) in the friends table or two(solid friends)
        }
    }

    public void addType(String type) {
        this.type = type;
    }
}
