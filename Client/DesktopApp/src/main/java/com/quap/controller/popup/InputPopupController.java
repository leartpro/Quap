package com.quap.controller.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class InputPopupController {
    @FXML
    private Button btnClose;

    @FXML
    private TextField userInput;
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
    void submit(ActionEvent event) {
        btnClose.setDisable(true);
        returnValue = userInput.getText();
        Stage stage = (Stage) Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        stage.close();
    }

    public String get() {
        return returnValue;
    }
}
