module org.example.healthcaremanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.mongodb.driver.sync.client;
    requires transitive org.mongodb.bson;
    requires org.mongodb.driver.core;
//    requires github.dotenv;
    requires java.dotenv;

    opens org.example.healthcaremanagementsystem to javafx.fxml;
    opens org.example.healthcaremanagementsystem.controller to javafx.fxml;
    opens org.example.healthcaremanagementsystem.model to javafx.base;

    exports org.example.healthcaremanagementsystem;
    exports org.example.healthcaremanagementsystem.model;
    exports org.example.healthcaremanagementsystem.controller;
}