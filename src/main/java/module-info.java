module com.example.tolab3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;


    opens com.example.tolab3 to javafx.fxml;
    exports com.example.tolab3;
}