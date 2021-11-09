package com.quap.controller.vista;

import javafx.scene.Node;

public abstract class VistaNavigator extends Node {

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    public abstract VistaNavigator getVistaByID(String id);

}
