package com.quap.controller.vista.login;

import com.quap.controller.VistaController;
import com.quap.controller.scene.LoginWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;

public class SignInController extends LoginVistaNavigator {
    private String username = "Anonym", password = "";
    private final LoginWindowController loginWindowController = VistaController.getLoginWindowController();

    @FXML
    private TextField txtUsername = new TextField();

    @FXML
    private Label lblPassword = new Label();

    @FXML
    private PasswordField txtPassword = new PasswordField();

    @FXML
    private Label lblUsername = new Label();

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

    /*@Override
    public void setRootNode(LoginWindowController loginWindowController) {
        this.loginWindowController = loginWindowController;
    }*/

    @FXML
    void validatePassword(KeyEvent keyEvent) {
        password = txtPassword.getText();
        if(password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}")) {
            lblPassword.setTextFill(Paint.valueOf("green"));
        } else if(password.length()==0) {
            lblPassword.setTextFill(Paint.valueOf("gray"));
        } else {
            lblPassword.setTextFill(Paint.valueOf("red"));
        }
        loginWindowController.toggleLogin(validLogin());
    }

    public void validateUsername(KeyEvent keyEvent) {
        username = txtUsername.getText();
        if(username.matches("[a-zA-Z]{4,12}")) {
            lblUsername.setTextFill(Paint.valueOf("green"));
        } else if(username.length()==0) {
            lblUsername.setTextFill(Paint.valueOf("gray"));
        } else {
            lblUsername.setTextFill(Paint.valueOf("red"));
        }
        loginWindowController.toggleLogin(validLogin());
    }

    @FXML
    void signUp(ActionEvent event) {
        VistaController.loadVista(VistaController.SignUp);
    }

}
