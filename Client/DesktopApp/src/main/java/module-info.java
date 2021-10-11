module com.quap.desktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens com.quap.desktopapp to javafx.fxml;
    exports com.quap.desktopapp;

    exports com.quap.controller;
    opens com.quap.controller to javafx.fxml;

    exports com.quap.controller.scene;
    opens com.quap.controller.scene to javafx.fxml;

    exports com.quap.controller.vista;
    opens com.quap.controller.vista to javafx.fxml;

    exports com.quap.controller.vista.sidebar to javafx.fxml;
    opens com.quap.controller.vista.sidebar to javafx.fxml;

    exports com.quap.controller.vista.content to javafx.fxml;
    opens com.quap.controller.vista.content to javafx.fxml;

}