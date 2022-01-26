package com.quap.controller.vista;

import javafx.scene.Node;

public abstract class VistaNavigator extends Node {

    protected abstract VistaNavigator getVistaByID(String id);
}
