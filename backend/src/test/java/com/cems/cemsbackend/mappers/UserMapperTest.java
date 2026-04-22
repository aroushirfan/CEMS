package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.User;
import com.cems.shared.model.UserDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

  @Test
  void toDTO_MapsFieldsCorrectly() {
    User user = new User();
    UUID id = UUID.randomUUID();
    LocalDate dob = LocalDate.of(2000, 1, 1);

    user.setId(id);
    user.setEmail("test@mail.com");
    user.setFirstName("Aroush");
    user.setMiddleName("B.");
    user.setLastName("Irfan");
    user.setAccessLevel(2);
    user.setPhone("12345");
    user.setDob(dob);
    user.setProfileImageUrl("img.png");

    UserDTO dto = UserMapper.toDTO(user);

    assertEquals(id, dto.getId());
    assertEquals("test@mail.com", dto.getEmail());
    assertEquals("Aroush", dto.getFirstName());
    assertEquals("B.", dto.getMiddleName());
    assertEquals("Irfan", dto.getLastName());
    assertEquals(2, dto.getAccessLevel());
    assertEquals("12345", dto.getPhone());
    assertEquals(dob, dto.getDob());
    assertEquals("img.png", dto.getProfileImageUrl());
  }

  @Test
  void updateEntity_UpdatesOnlyNonNullFields() {
    User user = new User();
    user.setFirstName("OldFirst");
    user.setMiddleName("OldMiddle");
    user.setLastName("OldLast");
    user.setPhone("000");
    user.setDob(LocalDate.of(1990, 1, 1));
    user.setProfileImageUrl("old.png");

    UserDTO dto = new UserDTO();
    dto.setFirstName("NewFirst");
    dto.setMiddleName(null); // should NOT overwrite
    dto.setLastName("NewLast");
    dto.setPhone("999");
    dto.setDob(null); // should NOT overwrite
    dto.setProfileImageUrl("new.png");

    UserMapper.updateEntity(user, dto);

    assertEquals("NewFirst", user.getFirstName());
    assertEquals("OldMiddle", user.getMiddleName()); // unchanged
    assertEquals("NewLast", user.getLastName());
    assertEquals("999", user.getPhone());
    assertEquals(LocalDate.of(1990, 1, 1), user.getDob()); // unchanged
    assertEquals("new.png", user.getProfileImageUrl());
  }

  @Test
  void updateEntity_DoesNothingWhenAllFieldsNull() {
    User user = new User();
    user.setFirstName("A");
    user.setMiddleName("B");
    user.setLastName("C");
    user.setPhone("123");
    user.setDob(LocalDate.of(2000, 1, 1));
    user.setProfileImageUrl("img.png");

    UserDTO dto = new UserDTO(); // all null

    UserMapper.updateEntity(user, dto);

    assertEquals("A", user.getFirstName());
    assertEquals("B", user.getMiddleName());
    assertEquals("C", user.getLastName());
    assertEquals("123", user.getPhone());
    assertEquals(LocalDate.of(2000, 1, 1), user.getDob());
    assertEquals("img.png", user.getProfileImageUrl());
  }


}
