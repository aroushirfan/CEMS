package com.cems.frontend.utils;

import com.cems.frontend.models.User;
import com.cems.shared.model.UserDTO;
import java.util.List;

/**
 * Utility mapper for converting user DTOs into frontend user models.
 */
public final class UserMapper {

  /**
   * Utility class constructor.
   */
  private UserMapper() {
  }

  /**
   * Converts a single user DTO into a user model.
   *
   * @param dto source user DTO
   * @return mapped user model
   */
  public static User toModel(UserDTO dto) {
    final User user = new User();
    user.setId(dto.getId());

    user.setFirstName(dto.getFirstName());
    user.setMiddleName(dto.getMiddleName());
    user.setLastName(dto.getLastName());
    user.setAccessLevel(dto.getAccessLevel());

    user.setPhone(dto.getPhone());
    user.setDob(dto.getDob());
    user.setEmail(dto.getEmail());


    user.setProfileImageUrl(dto.getProfileImageUrl());

    return user;
  }

  /**
   * Converts a list of user DTOs into user models.
   *
   * @param dtos list of source user DTOs
   * @return mapped list of user models
   */
  public static List<User> toModelList(List<UserDTO> dtos) {
    return dtos.stream()
        .map(UserMapper::toModel).toList();
  }
}

