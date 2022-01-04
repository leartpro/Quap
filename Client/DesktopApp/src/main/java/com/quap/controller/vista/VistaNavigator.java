package com.quap.controller.vista;

import javafx.scene.Node;

public abstract class VistaNavigator extends Node implements VistaObserver{

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    protected abstract VistaNavigator getVistaByID(String id);
}
