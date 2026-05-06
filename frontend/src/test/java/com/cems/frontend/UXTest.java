package com.cems.frontend;


import com.cems.frontend.services.AuthService;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.testfx.api.FxAssert.*;
import static org.testfx.matcher.control.LabeledMatchers.*;
import static org.hamcrest.Matchers.*;

import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

class UXTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            AuthService.getInstance().signUp("firstName", "lastName", "test@test.com", "testtest", "testtest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainApp().start(stage);
    }

    @BeforeAll
    static void beforeAll() {
        AuthService.getInstance().logout();
    }

    @Test
    void loginSearchAndRsvp() {
        clickOn("#loginLabel");
        waitForFxEvents();
        sleep(1000);
        clickOn("#emailField").write("test@test.com");
        clickOn("#passwordField").write("testtest");
        clickOn("#loginButton");
        waitForFxEvents();
        sleep(1000);
        clickOn("#allEventsButton");
        clickOn("#searchField").write("New Student M");
        waitForFxEvents();
        sleep(1000);
        clickOn((Node) lookup("#learnMoreButton").nth(0).query());
        waitForFxEvents();
        sleep(1000);
        clickOn("#registerNowButton");
        waitForFxEvents();
        Window messageWindow = window("Message");
        var alertText = lookup(".dialog-pane .content.label").from(messageWindow.getScene().getRoot()).nth(0);
        verifyThat(alertText, hasText(containsStringIgnoringCase("successful")));
        sleep(1000);
    }
}
