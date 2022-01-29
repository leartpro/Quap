package com.quap.controller.popup;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class DefaultPopupController {

    @FXML
    private ScrollPane scrollContent;
    @FXML
    private Button btnClose;
    @FXML
    private Label header;

    @FXML
    public void closePopup(ActionEvent actionEvent) {
        btnClose.setDisable(true);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void addContent(String content) {
        Label lbl = new Label();
        lbl.setText(content);
        lbl.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                        scrollContent.getViewportBounds().getWidth(),
                scrollContent.viewportBoundsProperty()));
        scrollContent.prefHeightProperty().bind(scrollContent.heightProperty());
        lbl.setAlignment(Pos.CENTER);
        scrollContent.setContent(lbl);
    }

    public void setHeader(String header) {
        this.header.setText(header);
    }
}
