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

public class SignUpController extends LoginVistaNavigator {

    private String username = "Anonym", password = "";
    private final List<LoginVistaObserver> observers = new ArrayList<>();

    @FXML
    private final TextField txtUsername = new TextField();

    @FXML
    private final Label lblPassword = new Label();

    @FXML
    private final PasswordField txtPassword = new PasswordField();

    @FXML
    private final Label lblPasswordConfirm = new Label();

    @FXML
    private final PasswordField txtPasswordConfirm = new PasswordField();

    @FXML
    private final Label lblUsername = new Label();

    @FXML
    void confirmPassword() {
        if(txtPassword.getText().equals(txtPasswordConfirm.getText())) {
            lblPasswordConfirm.setTextFill(Paint.valueOf("green"));
        } else if(txtPassword.getText().contains(txtPasswordConfirm.getText())) {
            lblPasswordConfirm.setTextFill(Paint.valueOf("yellow"));
        } else if(txtPasswordConfirm.getText().length()==0) {
            lblPasswordConfirm.setTextFill(Paint.valueOf("gray"));
        } else {
            lblPasswordConfirm.setTextFill(Paint.valueOf("red"));
        }
        for (LoginVistaObserver c : observers) {
            c.toggleLoginEvent(validLogin());
        }
    }

    @Override
    public boolean validLogin() {
        return password.equals(txtPasswordConfirm.getText())
                && username.matches("[a-zA-Z]{4,12}")
                && password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}");
    }

    @Override
    public void switchMode(boolean isSelected) {
        if(isSelected) {
            username = "Anonym";
            password = "";
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
            txtPasswordConfirm.setEditable(false);

            lblPassword.setTextFill(Paint.valueOf("gray"));
            lblPasswordConfirm.setTextFill(Paint.valueOf("gray"));
            lblUsername.setTextFill(Paint.valueOf("gray"));
        } else {
            if(validLogin()) {
                username = txtUsername.getText();
                password = txtPassword.getText();
                txtUsername.setEditable(true);
                txtPassword.setEditable(true);
                txtPasswordConfirm.setEditable(true);
            } else {
                username = "Anonym";
                password = "";
                txtUsername.setEditable(true);
                txtPassword.setEditable(true);
                txtPasswordConfirm.setEditable(true);
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
    void signIn() {
        for (LoginVistaObserver c : observers) {
            c.swapVistaEvent(VistaController.SignIn);
        }
    }

    public void addObserver(LoginVistaObserver observer) {
        observers.add(observer);
    }

}
