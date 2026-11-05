module com.cramer._2551_wiz_mart {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;

    opens com.cramer._2551_wiz_mart to javafx.fxml;
    opens com.cramer._2551_wiz_mart.controller to javafx.fxml;
    opens com.cramer._2551_wiz_mart.model to com.fasterxml.jackson.databind;

    exports com.cramer._2551_wiz_mart;
    exports com.cramer._2551_wiz_mart.controller;
    exports com.cramer._2551_wiz_mart.model;

}