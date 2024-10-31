module com.tankwars.frontend {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.net.http;
    requires com.google.gson;

    opens com.tankwars.frontend.controllers to javafx.fxml;
    opens com.tankwars.frontend to javafx.fxml;
    exports com.tankwars.frontend.controllers;
    exports com.tankwars.frontend.utils;
    exports com.tankwars.frontend.tankwarsclient to javafx.graphics;
}