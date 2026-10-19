module com.cramer._2551_wiz_mart {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.cramer._2551_wiz_mart to javafx.fxml;
    exports com.cramer._2551_wiz_mart;
    exports com.cramer._2551_wiz_mart.controller;
    opens com.cramer._2551_wiz_mart.controller to javafx.fxml;
}