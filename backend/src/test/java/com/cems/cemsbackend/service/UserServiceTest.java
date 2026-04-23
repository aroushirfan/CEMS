package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID id;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
    }

    @Test
    void getAllUsers_ReturnsList() {
        User user = new User();
        user.setId(id);
        user.setEmail("test@mail.com");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("test@mail.com", result.get(0).getEmail());
    }

    @Test
    void getAllUsers_ReturnsEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDTO> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUsers_ReturnsMultipleUsers() {
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@mail.com");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@mail.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void getUserById_ReturnsDTO() {
        User user = new User();
        user.setId(id);
        user.setEmail("test@mail.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO dto = userService.getUserById(id);

        assertEquals(id, dto.getId());
        assertEquals("test@mail.com", dto.getEmail());
    }

    @Test
    void getUserById_ThrowsWhenNotFound() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(id));
    }

    @Test
    void getUserById_ThrowsWithCorrectMessage() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(id));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getUserByEmail_ReturnsDTO() {
        User user = new User();
        user.setId(id);
        user.setEmail("hello@mail.com");

        when(userRepository.getUserByEmail("hello@mail.com")).thenReturn(Optional.of(user));

        UserDTO dto = userService.getUserByEmail("hello@mail.com");

        assertEquals("hello@mail.com", dto.getEmail());
    }

    @Test
    void getUserByEmail_ThrowsWhenNotFound() {
        when(userRepository.getUserByEmail("missing@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByEmail("missing@mail.com"));
    }

    @Test
    void getUserByEmail_ThrowsWithCorrectMessage() {
        when(userRepository.getUserByEmail("missing@mail.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserByEmail("missing@mail.com"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateAccessLevel_UpdatesUser() {
        User user = new User();
        user.setId(id);
        user.setAccessLevel(0);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.updateAccessLevel(id, 2);

        assertEquals(2, user.getAccessLevel());
        verify(userRepository).save(user);
    }

    @Test
    void updateAccessLevel_ThrowsWhenUserNotFound() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateAccessLevel(id, 2));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateAccessLevel_VerifiesSaveIsCalled() {
        User user = new User();
        user.setId(id);
        user.setAccessLevel(1);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.updateAccessLevel(id, 3);

        verify(userRepository).save(user);
    }

    @Test
    void updateCurrentUser_ById_UpdatesFields() {
        User user = new User();
        user.setId(id);
        user.setFirstName("Old");

        UserDTO dto = new UserDTO();
        dto.setFirstName("New");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO result = userService.updateCurrentUser(id, dto);

        assertEquals("New", result.getFirstName());
        verify(userRepository).save(user);
    }

    @Test
    void updateCurrentUser_ThrowsWhenUserNotFound() {
        UserDTO dto = new UserDTO();
        dto.setFirstName("New");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateCurrentUser(id, dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_DeletesById() {
        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void deleteUser_CanBeCalledMultipleTimes() {
        UUID id2 = UUID.randomUUID();

        userService.deleteUser(id);
        userService.deleteUser(id2);

        verify(userRepository).deleteById(id);
        verify(userRepository).deleteById(id2);
    }

    @Test
    void updateAccessLevel_WithHighAccessLevel() {
        User user = new User();
        user.setId(id);
        user.setAccessLevel(0);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.updateAccessLevel(id, 10);

        assertEquals(10, user.getAccessLevel());
        verify(userRepository).save(user);
    }

    @Test
    void updateCurrentUser_PreservesUserId() {
        User user = new User();
        user.setId(id);
        user.setEmail("original@mail.com");

        UserDTO dto = new UserDTO();
        dto.setFirstName("Updated");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO result = userService.updateCurrentUser(id, dto);

        assertEquals(id, result.getId());
    }
}
