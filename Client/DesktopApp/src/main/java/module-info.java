module com.quap.desktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires Client;

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

    exports com.quap.controller.popup to javafx.fxml;
    opens com.quap.controller.popup to javafx.fxml;

    exports com.quap.controller.vista to javafx.fxml;
    opens com.quap.controller.vista to javafx.fxml;

}