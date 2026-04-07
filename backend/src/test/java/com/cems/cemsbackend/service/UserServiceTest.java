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
    void deleteUser_DeletesById() {
        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }
}
