module shared {
    requires com.fasterxml.jackson.annotation;
    requires jakarta.validation;

//    requires com.fasterxml.jackson.databind;

    opens com.cems.shared.model to com.fasterxml.jackson.databind;

    exports com.cems.shared.model;
}