package com.quap.controller;

import com.quap.controller.scene.LoginWindowController;
import com.quap.controller.scene.MainWindowController;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/*
    stores general managing methods for vistas
*/
public class VistaController {
    public static final String LOGIN    = "/com/quap/desktopapp/scene/login-window.fxml";
    public static final String SignIn = "/com/quap/desktopapp/vista/login/signIn-login-vista.fxml";
    public static final String SignUp = "/com/quap/desktopapp/vista/login/signUp-login-vista.fxml";
    public static final String CHAT = "/com/quap/desktopapp/vista/main/chat-main-vista.fxml";
    public static final String PROFILE = "/com/quap/desktopapp/vista/main/profile-main-vista.fxml";
    public static final String SETTINGS = "/com/quap/desktopapp/vista/main/settings-main-vista.fxml";

    private static LoginWindowController loginWindowController;
    private static MainWindowController mainWindowController;

    public static void loadLoginVista(String fxml) {
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

    public static void loadMainVista(String fxml) {
        try {
            mainWindowController.setVista( //TODO: mainWindowController is null
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

    public static void setMainWindowController(MainWindowController mainWindowController) {
        VistaController.mainWindowController = mainWindowController;
    }
}
