package com.quap.controller;

import com.quap.controller.popup.PopupController;
import com.quap.utils.ResizeHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/*
    stores general managing methods for scenes
*/
public class SceneController {

    public void switchScene() {

    }

    public static void submitPopup(FXMLLoader loader, Stage primaryStage, String content) {
        Stage inputStage;
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PopupController popupController = loader.getController();
        popupController.addContent(content);
        inputStage = new Stage();
        inputStage.initStyle(StageStyle.UNDECORATED);
        inputStage.initOwner(primaryStage);
        inputStage.setScene(newScene);
        ResizeHelper.addResizeListener(inputStage);
        inputStage.showAndWait();
    }
}
