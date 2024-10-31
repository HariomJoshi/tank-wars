module com.tankwars.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens com.tankwars.frontend.controllers to javafx.fxml;
    exports com.tankwars.frontend.tankwarsclient to javafx.graphics;
    exports com.tankwars.frontend.controllers to javafx.fxml;

}