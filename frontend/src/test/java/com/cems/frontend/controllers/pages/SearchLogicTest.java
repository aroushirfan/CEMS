package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SearchLogicTest {

    @Test
    void testSearchFilterLogic() {
        ObservableList<Event> events = FXCollections.observableArrayList(
                new Event(null, "Metropolia Gala", "Desc", "Helsinki", 100, null, true, null),
                new Event(null, "Java Workshop", "Desc", "Espoo", 50, null, true, null)
        );

        FilteredList<Event> filteredList = new FilteredList<>(events);
        String searchInput = "helsinki";

        filteredList.setPredicate(event -> {
            String lowerCaseFilter = searchInput.toLowerCase();
            return event.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                    event.getLocation().toLowerCase().contains(lowerCaseFilter);
        });

        assertEquals(1, filteredList.size(), "Should only find the Helsinki event");
        assertEquals("Metropolia Gala", filteredList.get(0).getTitle());
    }
}