module com.example.cs230 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Ove module ćemo koristiti kao automatske module
    requires transitive jjwt.api;
    requires transitive jakarta.activation;

    opens com.example.cs230 to javafx.fxml;
    exports com.example.cs230;
}
