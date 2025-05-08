module com.amalitech.hospitalinformationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.amalitech.hospitalinformationsystem to javafx.fxml;
    opens com.amalitech.hospitalinformationsystem.controller to javafx.fxml;
    opens com.amalitech.hospitalinformationsystem.model to javafx.base;

    exports com.amalitech.hospitalinformationsystem;
    exports com.amalitech.hospitalinformationsystem.controller;
}