module org.cems.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    requires shared;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens org.cems.frontend.controllers.pages to javafx.fxml;
    opens org.cems.frontend.controllers.components to javafx.fxml;
    opens org.cems.frontend to javafx.fxml;
    opens org.cems.frontend.models to javafx.base;

    exports org.cems.frontend;
}