package com.quap.controller.scene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/*
    The login window of this application
 */
public class LoginWindowController {
    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPasswordConfirm;

    @FXML
    private CheckBox checkAnonymMode;

    @FXML
    private Button btnSubmit;
}
