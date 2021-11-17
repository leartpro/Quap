module com.quap.desktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires Client;
    requires java.prefs;
    requires java.sql;

    opens com.quap.desktopapp to javafx.fxml;
    exports com.quap.desktopapp;

    exports com.quap.controller;
    opens com.quap.controller to javafx.fxml;

    exports com.quap.controller.scene;
    opens com.quap.controller.scene to javafx.fxml;

    exports com.quap.controller.vista.login;
    opens com.quap.controller.vista.login;

    exports com.quap.controller.vista.main;
    opens com.quap.controller.vista.main to javafx.fxml;

}