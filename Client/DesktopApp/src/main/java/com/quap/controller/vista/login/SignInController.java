package com.quap.controller.vista.login;

import com.quap.controller.VistaController;
import com.quap.controller.vista.LoginVistaObserver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class SignInController extends LoginVistaNavigator {

    private String username = "Anonym", password = "";
    private final List<LoginVistaObserver> observers = new ArrayList<>();

    @FXML
    private final TextField txtUsername = new TextField();

    @FXML
    private final Label lblPassword = new Label();

    @FXML
    private final PasswordField txtPassword = new PasswordField();

    @FXML
    private final Label lblUsername = new Label();

    @Override
    public boolean validLogin() {
        return  username
                .matches("[a-zA-Z]{4,12}")
                && password
                .matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}");
    }

    @Override
    public void switchMode(boolean isSelected) {
        if(isSelected) {
            username = "Anonym";
            password = "";
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);

            lblPassword.setTextFill(Paint.valueOf("gray"));
            lblUsername.setTextFill(Paint.valueOf("gray"));
        } else {
            if(validLogin()) {
                username = txtUsername.getText();
                password = txtPassword.getText();
                txtUsername.setEditable(true);
                txtPassword.setEditable(true);
            } else {
                username = "Anonym";
                password = "";
                txtUsername.setEditable(true);
                txtPassword.setEditable(true);
            }
        }
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @FXML
    void validatePassword() {
        password = txtPassword.getText();
        LoginVistaNavigator.validatePassword(password, lblPassword);
        for (LoginVistaObserver c : observers) {
            c.toggleLoginEvent(validLogin());
        }
    }

    @FXML
    public void validateUsername() {
        username = txtUsername.getText();
        LoginVistaNavigator.validateUsername(username, lblUsername);
        for (LoginVistaObserver c : observers) {
            c.toggleLoginEvent(validLogin());
        }
    }

    @FXML
    void signUp() {
        for (LoginVistaObserver c : observers) {
            c.swapVistaEvent(VistaController.SignUp);
        }
    }


    public void addObserver(LoginVistaObserver observer) {
        observers.add(observer);
    }

}
