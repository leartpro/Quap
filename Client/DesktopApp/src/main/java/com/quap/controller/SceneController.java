package com.quap.controller;

import com.quap.controller.popup.DefaultPopupController;
import com.quap.controller.popup.InputPopupController;
import com.quap.controller.popup.RequestPopupController;
import com.quap.controller.popup.ReturnPopup;
import com.quap.utils.WindowMoveHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

/*
    stores general managing methods for scenes
*/
public class SceneController {

    public static void submitPopup(FXMLLoader loader, Stage primaryStage, String content, String header) {
        Stage inputStage;
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        DefaultPopupController popupController = loader.getController();
        popupController.addContent(content);
        inputStage = new Stage();
        inputStage.initStyle(StageStyle.UNDECORATED);
        inputStage.initOwner(primaryStage);
        inputStage.setScene(newScene);
        //ResizeHelper.addResizeListener(inputStage);
        WindowMoveHelper.addMoveListener(inputStage);
        popupController.setHeader(header);
        inputStage.showAndWait();
    }

    public static String submitInputPopup(FXMLLoader loader, Stage primaryStage, String header) {
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
        popupController.setHeader(header);
        return (String) inputStage.showAndReturn(popupController);
    }

    public static Boolean submitRequestPopup(FXMLLoader loader, Stage primaryStage, List<String> info, String header) {
        CallbackStage requestStage;
        Scene newScene = null;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        RequestPopupController popupController = loader.getController();
        requestStage = new CallbackStage();
        requestStage.initStyle(StageStyle.UNDECORATED);
        requestStage.initOwner(primaryStage);
        requestStage.setScene(newScene);
        WindowMoveHelper.addMoveListener(requestStage);
        popupController.setMessage(info);
        popupController.setHeader(header);
        return (Boolean) requestStage.showAndReturn(popupController);
    }

    private static class CallbackStage extends Stage {
        public Object showAndReturn(ReturnPopup controller) {
            super.showAndWait();
            return controller.get();
        }
    }
}
