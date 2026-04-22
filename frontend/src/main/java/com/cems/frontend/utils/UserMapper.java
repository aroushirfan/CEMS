package com.cems.frontend.utils;

import com.cems.frontend.models.User;
import com.cems.shared.model.UserDTO;

import java.time.LocalDate;
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

  /**
   * Create {@link UserDTO} from User Data for use in Settings page
   * @param currentUser Current user
   * @param email Email
   * @param phone Phone Number
   * @param date Date of Birth
   * @param fullName Full name
   * @param selectedProfileImagePath profile image path
   * @return {@link UserDTO}
   */
  public static UserDTO settingsToUserDTO(User currentUser, String email, String phone, LocalDate date, String fullName, String selectedProfileImagePath) {
    UserDTO dto = new UserDTO();
    dto.setId(currentUser.getId());
    dto.setEmail(email);

    dto.setPhone(phone);
    dto.setDob(date);

    dto.setAccessLevel(currentUser.getAccessLevel());


    String[] parts = fullName.trim().split("\\s+", 2);
    dto.setFirstName(parts.length > 0 ? parts[0] : "");
    dto.setLastName(parts.length > 1 ? parts[1] : "");

    // Profile image
    dto.setProfileImageUrl(
            selectedProfileImagePath != null
                    ? selectedProfileImagePath
                    : currentUser.getProfileImageUrl()
    );
    return dto;
  }
}

