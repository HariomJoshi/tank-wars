module com.tankwars.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.net.http;
    requires com.google.gson;

    opens com.tankwars.frontend to javafx.fxml;
    exports com.tankwars.frontend;
}