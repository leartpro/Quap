package com.quap.utils;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowMoveHelper {
    public WindowMoveHelper(Stage stage) {

    }

    public static void addMoveListener(Stage stage) {
        MoveListener moveListener = new MoveListener(stage);
        /*stage.getScene().addEventHandler(MouseEvent.DRAG_DETECTED, moveListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, moveListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED, moveListener);*/
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, moveListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, moveListener);
        ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
        for (Node child : children) {
            addListenerDeeply(child, moveListener);
        }
    }

    private static void addListenerDeeply(Node node, EventHandler<MouseEvent> listener) {
        /*node.addEventHandler(MouseEvent.DRAG_DETECTED, listener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, listener);*/
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);

        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (Node child : children) {
                addListenerDeeply(child, listener);
            }
        }
    }

    private static class MoveListener implements EventHandler<MouseEvent> {
        private final Stage stage;
        private double startScreenX = 0;
        private double startScreenY = 0;


        public MoveListener(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();

            if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
                startScreenX = mouseEvent.getScreenX();
                startScreenY = mouseEvent.getScreenY();
            } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType)) {
                stage.setX(stage.getX() + mouseEvent.getScreenX() - startScreenX);
                startScreenX = mouseEvent.getScreenX();
                stage.setY(stage.getY() + mouseEvent.getScreenY() - startScreenY);
                startScreenY = mouseEvent.getScreenY();
            }
        }
    }
}
