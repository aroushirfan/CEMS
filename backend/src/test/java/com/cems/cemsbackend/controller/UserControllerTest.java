package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.service.UserService;
import com.cems.shared.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

  @Test
  void getAllUsersShouldReturnList() {
    UserService userService = mock(UserService.class);
    UserController controller = new UserController(userService);

    UserDTO u1 = new UserDTO();
    u1.setId(UUID.randomUUID());
    u1.setEmail("a@test.com");

    UserDTO u2 = new UserDTO();
    u2.setId(UUID.randomUUID());
    u2.setEmail("b@test.com");

    when(userService.getAllUsers()).thenReturn(List.of(u1, u2));

    List<UserDTO> result = controller.getAllUsers();

    assertEquals(2, result.size());
    assertEquals("a@test.com", result.get(0).getEmail());
  }

  @Test
  void getUserByIdShouldReturnUser() {
    UserService userService = mock(UserService.class);
    UserController controller = new UserController(userService);

    UUID id = UUID.randomUUID();
    UserDTO dto = new UserDTO();
    dto.setId(id);
    dto.setEmail("user@test.com");

    when(userService.getUserById(id)).thenReturn(dto);

    UserDTO result = controller.getUserById(id);

    assertEquals(id, result.getId());
    assertEquals("user@test.com", result.getEmail());
  }

  @Test
  void updateAccessLevelShouldCallService() {
    UserService userService = mock(UserService.class);
    UserController controller = new UserController(userService);

    UUID id = UUID.randomUUID();
    UserDTO dto = new UserDTO();
    dto.setAccessLevel(3);

    controller.updateAccessLevel(id, dto);

    verify(userService).updateAccessLevel(id, 3);
  }
  @Test
  void getCurrentUserShouldReturnUser() {
    UserService userService = mock(UserService.class);
    UserController controller = new UserController(userService);

    UUID id = UUID.randomUUID();
    UserDTO dto = new UserDTO();
    dto.setId(id);
    dto.setEmail("me@test.com");

    when(userService.getUserById(id)).thenReturn(dto);

    var auth = new UsernamePasswordAuthenticationToken(id.toString(), null);

    UserDTO result = controller.getCurrentUser(auth);

    assertEquals(id, result.getId());
    assertEquals("me@test.com", result.getEmail());
  }

  @Test
  void updateCurrentUserShouldReturnUpdatedUser() {
    UserService userService = mock(UserService.class);
    UserController controller = new UserController(userService);

    UUID id = UUID.randomUUID();
    UserDTO input = new UserDTO();
    input.setEmail("updated@test.com");

    UserDTO updated = new UserDTO();
    updated.setId(id);
    updated.setEmail("updated@test.com");

    when(userService.updateCurrentUser(id, input)).thenReturn(updated);

    var auth = new UsernamePasswordAuthenticationToken(id.toString(), null);

    UserDTO result = controller.updateCurrentUser(auth, input);

    assertEquals("updated@test.com", result.getEmail());
  }

  @Test
  void deleteCurrentUserShouldCallService() {
    UserService userService = mock(UserService.class);
    UserController controller = new UserController(userService);

    UUID id = UUID.randomUUID();
    var auth = new UsernamePasswordAuthenticationToken(id.toString(), null);

    controller.deleteCurrentUser(auth);

    verify(userService).deleteUser(id);
  }
}