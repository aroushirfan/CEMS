module org.cems.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    // Open hybrid sub-packages instead
    opens org.cems.frontend.controllers.pages to javafx.fxml;
    opens org.cems.frontend.controllers.components to javafx.fxml;

    // Allows FXML to find MainApp or common view classes
    opens org.cems.frontend to javafx.fxml;

    opens org.cems.frontend.models to javafx.base;

    exports org.cems.frontend;
}