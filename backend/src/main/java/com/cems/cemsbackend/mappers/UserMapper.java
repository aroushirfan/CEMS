//package com.cems.cemsbackend.mappers;
//
//import com.cems.cemsbackend.model.User;
//import com.cems.shared.model.UserDTO;
//
//public class UserMapper {
//    public static UserDTO toDTO(User user) {
//        return new UserDTO(
//                user.getId(),
//                user.getEmail(),
//                user.getAccessLevel(),
//                user.getFirstName(),
//                user.getMiddleName(),
//                user.getLastName()
//        );
//    }
//}

package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.User;
import com.cems.shared.model.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();

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

    public static void updateEntity(User user, UserDTO dto) {



        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());

        if (dto.getMiddleName() != null)
            user.setMiddleName(dto.getMiddleName());

        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());


        if (dto.getPhone() != null)
            user.setPhone(dto.getPhone());

        if (dto.getDob() != null)
            user.setDob(dto.getDob());

        if (dto.getProfileImageUrl() != null)
            user.setProfileImageUrl(dto.getProfileImageUrl());
    }

}
