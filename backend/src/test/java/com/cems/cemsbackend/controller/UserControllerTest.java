package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.service.UserService;
import com.cems.shared.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    @Test
    void getAllUsersShouldReturnList() {
        UserService userService = Mockito.mock(UserService.class);
        UserController controller = new UserController(userService);
        UserDTO user1 = new UserDTO();
        user1.setId(UUID.randomUUID());
        user1.setEmail("test1@test.com");
        UserDTO user2 = new UserDTO();
        user2.setId(UUID.randomUUID());
        user2.setEmail("test2@test.com");
        List<UserDTO> mockList = List.of(user1, user2);
        Mockito.when(userService.getAllUsers()).thenReturn(mockList);
        List<UserDTO> result = controller.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("test1@test.com", result.get(0).getEmail());
    }

    @Test
    void getUserByIdShouldReturnUser() {
        UserService userService = Mockito.mock(UserService.class);
        UserController controller = new UserController(userService);
        UUID id = UUID.randomUUID();
        UserDTO mockUser = new UserDTO();
        mockUser.setId(id);
        mockUser.setEmail("user@test.com");
        Mockito.when(userService.getUserById(id)).thenReturn(mockUser);
        UserDTO result = controller.getUserById(id);
        assertEquals(id, result.getId());
        assertEquals("user@test.com", result.getEmail());
    }

    @Test
    void updateAccessLevelShouldCallService() {
        UserService userService = Mockito.mock(UserService.class);
        UserController controller = new UserController(userService);
        UUID id = UUID.randomUUID();
        UserDTO dto = new UserDTO();
        dto.setAccessLevel(2);
        controller.updateAccessLevel(id, dto);
        Mockito.verify(userService).updateAccessLevel(id, 2);
    }
}
