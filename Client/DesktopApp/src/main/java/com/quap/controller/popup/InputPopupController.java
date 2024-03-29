package com.quap.controller.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InputPopupController extends ReturnPopup {
    @FXML
    private Button btnClose;

    @FXML
    private TextField userInput;
    @FXML
    private Label header;

    private String returnValue;

    @FXML
    void closePopup(ActionEvent actionEvent) {
        btnClose.setDisable(true);
        returnValue = null;
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    //gives to user request back to the client object
    @FXML
    void submit(ActionEvent actionEvent) {
        btnClose.setDisable(true);
        returnValue = userInput.getText();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public String get() {
        return returnValue;
    }

    public void setHeader(String header) {
        this.header.setText(header);
    }
}
