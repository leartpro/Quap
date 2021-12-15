package com.quap.controller;

import com.quap.controller.scene.LoginWindowController;
import com.quap.controller.scene.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/*
    stores general managing methods for vistas
*/
public class VistaController {
    public static final String LOGIN    = "/com/quap/desktopapp/scene/login-window.fxml";
    public static final String SignIn = "/com/quap/desktopapp/vista/login/signIn-login-vista.fxml";
    public static final String SignUp = "/com/quap/desktopapp/vista/login/signUp-login-vista.fxml";
    public static final String CHAT = "/com/quap/desktopapp/vista/main/chat-main-vista.fxml";
    // --Commented out by Inspection (15.12.2021 21:15):public static final String PROFILE = "/com/quap/desktopapp/vista/main/profile-main-vista.fxml";
    // --Commented out by Inspection (15.12.2021 21:15):public static final String SETTINGS = "/com/quap/desktopapp/vista/main/settings-main-vista.fxml";
    public static final String LIST = "/com/quap/desktopapp/vista/main/list-main-vista.fxml";

    private static LoginWindowController loginWindowController;
    private static MainWindowController mainWindowController;

    public static void loadLoginVista(String fxml) {
        Parent node;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(VistaController.class.getResource(fxml));
            node = loader.load();
            loginWindowController.setVista(node, loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMainVista(String fxml) {
        Parent node;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(VistaController.class.getResource(fxml));
            node = loader.load();
            mainWindowController.setVista(node, loader.getController());
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
