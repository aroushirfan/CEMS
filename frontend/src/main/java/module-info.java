module org.cems.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.cems.frontend.controllers to javafx.fxml;
    opens org.cems.frontend.models to javafx.base;
    exports org.cems.frontend;
}