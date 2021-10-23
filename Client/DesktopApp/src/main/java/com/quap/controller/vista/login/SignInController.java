package com.quap.controller.vista.login;

import com.quap.controller.VistaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SignInController {
    @FXML
    void signUp(ActionEvent event) {
        VistaController.loadVista(VistaController.SignUp);
    }
}
