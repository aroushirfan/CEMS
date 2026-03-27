//package com.cems.cemsbackend.controller;
//
//import com.cems.cemsbackend.service.UserService;
//import com.cems.shared.model.UserDTO;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    // GET all users
//    @GetMapping
//    public List<UserDTO> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    // GET single user
//    @GetMapping("/{id}")
//    public UserDTO getUserById(@PathVariable UUID id) {
//        return userService.getUserById(id);
//    }
//
//    // UPDATE access level
//    @PutMapping("/{id}/access-level")
//    public void updateAccessLevel(
//            @PathVariable UUID id,
//            @RequestBody UserDTO dto
//    ) {
//        userService.updateAccessLevel(id, dto.getAccessLevel());
//    }
//}

package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.service.UserService;
import com.cems.shared.model.UserDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET all users (ADMIN ONLY)
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET single user (ADMIN ONLY)
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    // UPDATE access level (ADMIN ONLY)
    @PutMapping("/{id}/access-level")
    public void updateAccessLevel(
            @PathVariable UUID id,
            @RequestBody UserDTO dto
    ) {
        userService.updateAccessLevel(id, dto.getAccessLevel());
    }

    @GetMapping("/me")
    public UserDTO getCurrentUser(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        return userService.getUserById(userId);
    }

    @PutMapping("/me")
    public UserDTO updateCurrentUser(Authentication auth, @RequestBody UserDTO dto) {
        UUID userId = UUID.fromString(auth.getName());
        return userService.updateCurrentUser(userId, dto);
    }

    @DeleteMapping("/me")
    public void deleteCurrentUser(Authentication auth) {
        UUID userId = UUID.fromString(auth.getName());
        userService.deleteUser(userId);
    }
}
