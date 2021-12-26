package com.quap.desktopapp;

import com.quap.utils.WindowMoveHelper;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class LauncherPreloader extends Preloader {

    private Stage preLoaderStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/quap/desktopapp/scene/connection-window.fxml")));
        Scene scene;
        String osName = System.getProperty("os.name");
        if( osName != null && osName.startsWith("Windows") ) {
            scene = (new ShadowScene()).getShadowScene(root);
            primaryStage.initStyle(StageStyle.TRANSPARENT);

        } else {
            scene = new Scene(root);
            primaryStage.initStyle(StageStyle.UNDECORATED);
        }
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();
        WindowMoveHelper.addMoveListener(primaryStage);
        this.preLoaderStage = primaryStage;
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        super.handleStateChangeNotification(info);
        if(info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preLoaderStage.hide();
        }
    }

    public static class ShadowScene {
        public Scene getShadowScene(Parent p) {
            Scene scene;
            AnchorPane outer = new AnchorPane();
            outer.getChildren().add( p );
            outer.setPadding(new Insets(10.0d));
            outer.setBackground( new Background(new BackgroundFill( Color.rgb(0,0,0,0), new CornerRadii(0), new
                    Insets(0))));

            p.setEffect(new DropShadow());
            ((VBox)p).setBackground( new Background(new BackgroundFill( Color.WHITE, new CornerRadii(0), new Insets(0)
            )));
            scene = new Scene( outer );
            scene.setFill( Color.rgb(0,255,0,0) );
            return scene;
        }
    }
}
