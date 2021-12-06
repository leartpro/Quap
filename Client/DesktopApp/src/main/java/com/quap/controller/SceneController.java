package com.quap.controller;

import com.quap.controller.popup.InputPopupController;
import com.quap.controller.popup.PopupController;
import com.quap.utils.WindowMoveHelper;
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
        //ResizeHelper.addResizeListener(inputStage);
        WindowMoveHelper.addMoveListener(inputStage);
        inputStage.showAndWait();
    }

    public static String submitInputPopup(FXMLLoader loader, Stage primaryStage) {
        CallbackStage inputStage;
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        InputPopupController popupController = loader.getController();
        inputStage = new CallbackStage();
        inputStage.initStyle(StageStyle.UNDECORATED);
        inputStage.initOwner(primaryStage);
        inputStage.setScene(newScene);
        WindowMoveHelper.addMoveListener(inputStage);
        String input = inputStage.showAndReturn(popupController);
        return input;
    }

    private static class CallbackStage extends Stage {
        public String showAndReturn(InputPopupController controller) {
            super.showAndWait();
            return controller.get();
        }
    }
}
