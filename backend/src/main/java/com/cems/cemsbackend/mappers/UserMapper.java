package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.User;
import com.cems.shared.model.UserDTO;

/**
 * Utility class to map between User entity and UserDTO.
 */
public final class UserMapper {

  /**
   * Private constructor to prevent instantiation of utility class.
   */
  private UserMapper() {
    // Prevent instantiation
  }

  // CHECKSTYLE:OFF: AbbreviationAsWordInName
  /**
   * Converts a User entity to a UserDTO.
   *
   * @param user the user entity to convert.
   * @return the converted user DTO.
   */
  public static UserDTO toDTO(final User user) {
    final UserDTO dto = new UserDTO();

    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setAccessLevel(user.getAccessLevel());
    dto.setFirstName(user.getFirstName());
    dto.setMiddleName(user.getMiddleName());
    dto.setLastName(user.getLastName());
    dto.setPhone(user.getPhone());
    dto.setDob(user.getDob());
    dto.setProfileImageUrl(user.getProfileImageUrl());

    return dto;
  }
  // CHECKSTYLE:ON: AbbreviationAsWordInName

  /**
   * Updates an existing User entity with values from a UserDTO.
   *
   * @param user the user entity to update.
   * @param dto  the DTO containing new values.
   */
  public static void updateEntity(final User user, final UserDTO dto) {
    if (dto.getFirstName() != null) {
      user.setFirstName(dto.getFirstName());
    }

    if (dto.getMiddleName() != null) {
      user.setMiddleName(dto.getMiddleName());
    }

    if (dto.getLastName() != null) {
      user.setLastName(dto.getLastName());
    }

    if (dto.getPhone() != null) {
      user.setPhone(dto.getPhone());
    }

    if (dto.getDob() != null) {
      user.setDob(dto.getDob());
    }

    if (dto.getProfileImageUrl() != null) {
      user.setProfileImageUrl(dto.getProfileImageUrl());
    }
  }
}