package com.cems.frontend.services;

import com.cems.frontend.models.User;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private MockWebServer server;
    private UserService userService;

    @BeforeEach
    void setup() throws Exception {
        server = new MockWebServer();
        server.start();

        LocalHttpClientHelper.setPort(String.valueOf(server.getPort()));
        userService = new UserService();
    }

    @AfterEach
    void teardown() throws Exception {
        server.shutdown();
    }

    @Test
    void getAllUsers() throws Exception {
        String json = """
            [
              {"email":"a@test.com","access_level":0},
              {"email":"b@test.com","access_level":1}
            ]
        """;

        server.enqueue(new MockResponse().setResponseCode(200).setBody(json));
        server.enqueue(new MockResponse().setResponseCode(204));

        List<User> users1 = userService.getAllUsers();
        assertEquals(2, users1.size());
        assertEquals("a@test.com", users1.get(0).getEmail());

        List<User> users2 = userService.getAllUsers();
        assertTrue(users2.isEmpty());
    }

    @Test
    void getUserById() throws Exception {
        String json = """
            {"email":"x@test.com","access_level":0}
        """;

        server.enqueue(new MockResponse().setResponseCode(200).setBody(json));

        User user = userService.getUserById("123");

        assertEquals("x@test.com", user.getEmail());
    }

    @Test
    void updateAccessLevel() {
        server.enqueue(new MockResponse().setResponseCode(200));
        server.enqueue(new MockResponse().setResponseCode(400));

        assertDoesNotThrow(() -> userService.updateAccessLevel("123", 1));
        assertThrows(IOException.class, () -> userService.updateAccessLevel("123", 1));
    }
}