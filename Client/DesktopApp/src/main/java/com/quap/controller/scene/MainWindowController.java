package com.quap.controller.scene;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainWindowController {
    @FXML
    private final StackPane vistaHolder;

    public MainWindowController() {
        vistaHolder = new StackPane();
    }

    /**
     * Replaces the vista displayed in the vista holder with a new vista.
     *
     * @param node the vista node to be swapped in.
     */
    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }
}
