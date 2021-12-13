package com.quap.controller.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RequestPopupController extends ReturnPopup{
    @FXML
    private TextField txtDisplay;

    @FXML
    private Button btnAccept;

    @FXML
    private Button btnDecline;

    boolean decision;

    @FXML
    void accept(ActionEvent actionEvent) {
        decision = true;
        btnDecline.setDisable(true);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void decline(ActionEvent actionEvent) {
        decision = false;
        btnDecline.setDisable(true);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setMessage(String message) {
        txtDisplay.setText(message);
    }

    @Override
    public Boolean get() {
        return decision;
    }
}
