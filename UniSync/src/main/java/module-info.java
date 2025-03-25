module com.example.UniSync {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.UniSync to javafx.fxml;
    exports com.example.UniSync;
}