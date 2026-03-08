package com.cems.frontend.utils;

import com.cems.frontend.models.User;
import com.cems.shared.model.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toModel(UserDTO dto) {
        return new User(
                dto.getId(),
                dto.getEmail(),
                dto.getAccessLevel(),
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getLastName()
        );
    }

    public static List<User> toModelList(List<UserDTO> dtos) {
        return dtos.stream()
                .map(UserMapper::toModel)
                .collect(Collectors.toList());
    }
}
