package com.quap.controller.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

/*
    The login window of this application
 */
public class LoginWindowController {
    //TODO: change content with vistas

    private String name, password;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblPasswordConfirm;

    @FXML
    private PasswordField txtPasswordConfirm;

    @FXML
    private CheckBox checkAnonymMode;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblUsername;

    @FXML
    private StackPane stackContent;

    @FXML
    public void initialize() {
        checkAnonymMode.setSelected(false);
        btnLogin.setVisible(false);
    }

    @FXML
    void confirmPassword(KeyEvent keyEvent) {
        if(txtPassword.getText().equals(txtPasswordConfirm.getText())) {
            lblPasswordConfirm.setTextFill(Paint.valueOf("green"));
        } else if(txtPassword.getText().contains(txtPasswordConfirm.getText())) {
            lblPasswordConfirm.setTextFill(Paint.valueOf("yellow"));
        } else if(txtPasswordConfirm.getText().length()==0) {
            lblPasswordConfirm.setTextFill(Paint.valueOf("gray"));
        } else {
            lblPasswordConfirm.setTextFill(Paint.valueOf("red"));
        }
        btnLogin.setVisible(validLogin());
    }

    @FXML
    void login(ActionEvent event) {
        //confirm username with server
        //check for lokal profil
        //load main
    }

    @FXML
    void switchMode(ActionEvent event) {
        if(checkAnonymMode.isSelected()) {
            name = "Anonym";
            password = "";
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
            txtPasswordConfirm.setEditable(false);
            btnLogin.setVisible(true);

            lblPassword.setTextFill(Paint.valueOf("gray"));
            lblPasswordConfirm.setTextFill(Paint.valueOf("gray"));
            lblUsername.setTextFill(Paint.valueOf("gray"));
        } else { //TODO: undo color change from lblPassword, lblPasswordConfirm and lblUsername
            if(validLogin()) {
                name = txtUsername.getText();
                password = txtPassword.getText();
                txtUsername.setEditable(true);
                txtPassword.setEditable(true);
                txtPasswordConfirm.setEditable(true);
                btnLogin.setVisible(true);
            } else {
                name = "Anonym";
                password = "";
                txtUsername.setEditable(true);
                txtPassword.setEditable(true);
                txtPasswordConfirm.setEditable(true);
                btnLogin.setVisible(false);
            }
        }
    }

    private boolean validLogin() {
        return txtPassword.getText().equals(txtPasswordConfirm.getText())
                && txtUsername.getText().matches("[a-zA-Z]{4,12}")
                && txtPassword.getText().matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}");
    }

    @FXML
    void validatePassword(KeyEvent keyEvent) {
        String password = txtPassword.getText();
        if(password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}")) {
            lblPassword.setTextFill(Paint.valueOf("green"));
        } else if(password.length()==0) {
            lblPassword.setTextFill(Paint.valueOf("gray"));
        } else {
            lblPassword.setTextFill(Paint.valueOf("red"));
        }
        btnLogin.setVisible(validLogin());
    }



    public void loadMain(ActionEvent e) throws IOException {
        //TODO: give attributes to main scene controller
        //TODO: set size (not resizable???)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/quap/desktopapp/scene/main-window.fxml"));
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void validateUsername(KeyEvent keyEvent) {
        String username = txtUsername.getText();
        if(username.matches("[a-zA-Z]{4,12}")) {
            lblUsername.setTextFill(Paint.valueOf("green"));
        } else if(username.length()==0) {
            lblUsername.setTextFill(Paint.valueOf("gray"));
        } else {
            lblUsername.setTextFill(Paint.valueOf("red"));
        }
        btnLogin.setVisible(validLogin());
    }
}
