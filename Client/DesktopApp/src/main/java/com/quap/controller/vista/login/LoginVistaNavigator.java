package com.quap.controller.vista.login;

import com.quap.controller.vista.LoginVistaObserver;
import com.quap.controller.vista.VistaNavigator;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public abstract class LoginVistaNavigator extends VistaNavigator{

    public static void validatePassword(String password, Label lblPassword) {
        if (password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}")) {
            lblPassword.setTextFill(Paint.valueOf("green"));
        } else if (password.length() == 0) {
            lblPassword.setTextFill(Paint.valueOf("gray"));
        } else {
            lblPassword.setTextFill(Paint.valueOf("red"));
        }
    }

    public static void validateUsername(String username, Label lblUsername) {
        if (username.matches("[a-zA-Z]{4,12}")) {
            lblUsername.setTextFill(Paint.valueOf("green"));
        } else if (username.length() == 0) {
            lblUsername.setTextFill(Paint.valueOf("gray"));
        } else {
            lblUsername.setTextFill(Paint.valueOf("red"));
        }
    }

    public abstract boolean validLogin();

    public abstract void switchMode(boolean isSelected);

    public abstract String getName();

    public abstract String getPassword();

    public abstract void addObserver(LoginVistaObserver observer);

}
