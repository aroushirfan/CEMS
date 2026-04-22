package com.cems.frontend.utils;

import com.cems.frontend.models.User;
import com.cems.shared.model.UserDTO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


class UserMapperTest {

    @Test
    void shouldMapUserDTOToUser() {
        // given
        UserDTO dto = new UserDTO();
        dto.setId(UUID.randomUUID());
        dto.setFirstName("John");
        dto.setMiddleName("A");
        dto.setLastName("Doe");
        dto.setAccessLevel(2);
        dto.setPhone("123456789");
        dto.setDob(LocalDate.of(1990, 1, 1));
        dto.setEmail("john.doe@example.com");
        dto.setProfileImageUrl("http://image.com/profile.jpg");

        // when
        User user = UserMapper.toModel(dto);

        // then
        assertNotNull(user);
        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getMiddleName(), user.getMiddleName());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.getAccessLevel(), user.getAccessLevel());
        assertEquals(dto.getPhone(), user.getPhone());
        assertEquals(dto.getDob(), user.getDob());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getProfileImageUrl(), user.getProfileImageUrl());
    }

    @Test
    void shouldMapListOfUserDTOsToUsers() {
        // given
        UserDTO dto1 = new UserDTO();
        dto1.setId(UUID.randomUUID());
        dto1.setFirstName("John");

        UserDTO dto2 = new UserDTO();
        dto2.setId(UUID.randomUUID());
        dto2.setFirstName("Jane");

        List<UserDTO> dtos = List.of(dto1, dto2);

        List<User> users = UserMapper.toModelList(dtos);

        assertNotNull(users);
        assertEquals(2, users.size());

        assertEquals(dto1.getId(), users.get(0).getId());
        assertEquals(dto1.getFirstName(), users.get(0).getFirstName());

        assertEquals(dto2.getId(), users.get(1).getId());
        assertEquals(dto2.getFirstName(), users.get(1).getFirstName());
    }

    @Test
    void shouldHandleEmptyList() {
        List<UserDTO> dtos = List.of();
        List<User> users = UserMapper.toModelList(dtos);

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDtoIsNull() {
        assertThrows(NullPointerException.class, () -> UserMapper.toModel(null));
    }

    @Test
    @Disabled
    void settingsToUserDTO() {
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            UUID id = UUID.randomUUID();
            String email = RandomString.generateRandomString(10);
            int accessLevel = random.nextInt(0, 3);
            String firstName = RandomString.generateRandomString(10);
            String lastName = RandomString.generateRandomString(10);
            String phone = RandomString.generateRandomString(8);
            String profileImage = RandomString.generateRandomString(10);


            User currentUser = new User(id, "email", accessLevel, "firstName", "middleName", "lastName");
            currentUser.setProfileImageUrl("profile");
            currentUser.setPhone("");
            LocalDate date = LocalDate.ofEpochDay(
                    ThreadLocalRandom.current().nextLong(
                            LocalDate.of(2000, 1, 1).toEpochDay(),
                            LocalDate.of(2025, 12, 31).toEpochDay()
                    )
            );


            UserDTO refDTO = new UserDTO();
            refDTO.setDob(date);
            refDTO.setId(id);
            refDTO.setEmail(email);
            refDTO.setFirstName(firstName);
            refDTO.setLastName(lastName);
            refDTO.setAccessLevel(accessLevel);
            refDTO.setPhone(phone);
            refDTO.setProfileImageUrl(profileImage);


            UserDTO testDTO = UserMapper.settingsToUserDTO(currentUser, email, phone, date, firstName + " " + lastName, profileImage);

            assertEquals(refDTO, testDTO);
        }
    }
}