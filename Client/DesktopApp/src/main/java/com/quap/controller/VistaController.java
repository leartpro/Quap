package com.quap.controller;

import com.quap.controller.scene.WindowController;
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

    public static void loadVista(String fxml, WindowController controller) {
        Parent node;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(VistaController.class.getResource(fxml));
            node = loader.load();
            controller.setVista(node, loader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
