module org.example.planifyfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.zaxxer.hikari;

    exports org.example.planifyfx.model;
    exports org.example.planifyfx.repository;
    exports org.example.planifyfx.util;
    exports org.example.planifyfx.controller;
    exports org.example.planifyfx;

    opens org.example.planifyfx.controller to javafx.fxml;
}