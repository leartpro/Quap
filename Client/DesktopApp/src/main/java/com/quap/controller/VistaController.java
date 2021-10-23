package com.quap.controller;

import com.quap.controller.scene.LoginWindowController;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/*
    stores general managing methods for vistas
*/
public class VistaController {
    public static final String LOGIN    = "/com/quap/desktopapp/scene/login-window.fxml";
    public static final String SignIn = "/com/quap/desktopapp/vista/signIn-login-vista.fxml";
    public static final String SignUp = "/com/quap/desktopapp/vista/signUp-login-vista.fxml";

    private static LoginWindowController loginWindowController;

    public static void loadVista(String fxml) {
        try {
            loginWindowController.setVista(
                    FXMLLoader.load(
                            VistaController.class.getResource(
                                    fxml
                            )
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLoginWindowController(LoginWindowController loginWindowController) {
        VistaController.loginWindowController = loginWindowController;
    }

    public static LoginWindowController getLoginWindowController() {
        return loginWindowController;
    }

}
