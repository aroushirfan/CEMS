module com.cems.frontend {




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
    requires java.prefs;
  requires net.bytebuddy;

  opens com.cems.frontend.controllers.pages to javafx.fxml;
    opens com.cems.frontend.controllers.components to javafx.fxml;

    opens com.cems.frontend to javafx.fxml;

    exports com.cems.frontend;
    opens com.cems.frontend.models to com.fasterxml.jackson.databind, javafx.base, javafx.fxml;


}
