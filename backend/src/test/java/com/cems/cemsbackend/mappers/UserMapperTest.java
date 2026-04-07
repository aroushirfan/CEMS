package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.User;
import com.cems.shared.model.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toDTO_MapsFieldsCorrectly() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setFirstName("Aroush");
        user.setLastName("Irfan");
        user.setAccessLevel(2);

        UserDTO dto = UserMapper.toDTO(user);

        assertEquals("test@mail.com", dto.getEmail());
        assertEquals("Aroush", dto.getFirstName());
        assertEquals("Irfan", dto.getLastName());
        assertEquals(2, dto.getAccessLevel());
    }
}