package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.User;
import com.cems.shared.model.UserDTO;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getAccessLevel(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName()
        );
    }
}
