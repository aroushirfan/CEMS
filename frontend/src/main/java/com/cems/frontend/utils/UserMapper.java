package com.cems.frontend.utils;

import com.cems.frontend.models.User;
import com.cems.shared.model.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {}

    public static User toModel(UserDTO dto) {
        User user = new User();
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

    public static List<User> toModelList(List<UserDTO> dtos) {
        return dtos.stream()
                .map(UserMapper::toModel)
                .collect(Collectors.toList());
    }
}

