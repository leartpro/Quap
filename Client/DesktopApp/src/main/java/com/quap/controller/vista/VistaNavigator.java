package com.quap.controller.vista;

import com.quap.controller.vista.login.SignInController;
import com.quap.controller.vista.login.SignUpController;
import javafx.scene.Node;

public abstract class VistaNavigator extends Node {


    public static VistaNavigator getVistaByID(String id) {
        if(id.equals("signUp")) {
            return new SignUpController();
        } else if(id.equals("signIn")) {
            return new SignInController();
        } else {
            IllegalArgumentException e;
            return null;
        }
    }

    public abstract boolean validLogin();

    public abstract void switchMode(boolean isSelected);

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    //public abstract void setRootNode(LoginWindowController loginWindowController);
}
