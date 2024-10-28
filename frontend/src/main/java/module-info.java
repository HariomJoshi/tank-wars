module com.tankwars.frontend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tankwars.frontend to javafx.fxml;
    exports com.tankwars.frontend;
}