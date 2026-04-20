package com.cems.frontend.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathsTest {

    @Test
    void testAllPathsUseCorrectPrefixes() {
        for (Paths value : Paths.values()) {
            assertTrue(value.path.startsWith("/com/cems/frontend/view/"));
            assertTrue(value.bundlePath.startsWith("com.cems.frontend.view.i18n."));
        }
    }

    @Test
    void testHomePathAndBundleMatchExpectedValues() {
        assertEquals("/com/cems/frontend/view/pages/MainHome.fxml", Paths.HOME.path);
        assertEquals("com.cems.frontend.view.i18n.MainHome", Paths.HOME.bundlePath);
    }
}
