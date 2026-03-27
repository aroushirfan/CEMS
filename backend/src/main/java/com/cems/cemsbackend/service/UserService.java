//package com.cems.cemsbackend.service;
//
//import com.cems.cemsbackend.model.User;
//import com.cems.cemsbackend.repository.UserRepository;
//import com.cems.cemsbackend.mappers.UserMapper;
//import com.cems.shared.model.UserDTO;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
////@Service
////public class UserService {
////
////    private final UserRepository userRepository;
////
////    public UserService(UserRepository userRepository) {
////        this.userRepository = userRepository;
////    }
////
////    public List<UserDTO> getAllUsers() {
////        return userRepository.findAll()
////                .stream()
////                .map(UserMapper::toDTO)
////                .toList();
////    }
////
////    public UserDTO getUserById(UUID id) {
////        return userRepository.findById(id)
////                .map(UserMapper::toDTO)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////    }
////
////    public void updateAccessLevel(UUID id, int accessLevel) {
////        User user = userRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        user.setAccessLevel(accessLevel);
////        userRepository.save(user);
////    }
////}
//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public List<UserDTO> getAllUsers() {
//        return userRepository.findAll()
//                .stream()
//                .map(UserMapper::toDTO)
//                .toList();
//    }
//
//    public UserDTO getUserById(UUID id) {
//        return userRepository.findById(id)
//                .map(UserMapper::toDTO)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    public void updateAccessLevel(UUID id, int accessLevel) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setAccessLevel(accessLevel);
//        userRepository.save(user);
//    }
//
//    public UserDTO getCurrentUser(User principal) {
//        return UserMapper.toDTO(principal);
//
//    }
//
//    public UserDTO updateCurrentUser(User principal, UserDTO dto) {
//        UserMapper.updateEntity(principal, dto);
//        userRepository.save(principal);
//        return UserMapper.toDTO(principal);
//    }
//    public UserDTO getUserByEmail(String email) {
//        User user = userRepository.getUserByEmail(email)
//
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return UserMapper.toDTO(user);
//    }
//
//    public UserDTO updateCurrentUser(String email, UserDTO dto) {
//        User user = userRepository.getUserByEmail(email)
//
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserMapper.updateEntity(user, dto);
//        userRepository.save(user);
//
//        return UserMapper.toDTO(user);
//    }
//
//    public UserDTO updateCurrentUser(UUID userId, UserDTO dto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserMapper.updateEntity(user, dto);
//        userRepository.save(user);
//
//        return UserMapper.toDTO(user);
//    }
//
//
//}
package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.cemsbackend.mappers.UserMapper;
import com.cems.shared.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // -------------------------
    // GET ALL USERS
    // -------------------------
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // -------------------------
    // GET USER BY ID
    // -------------------------
    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // -------------------------
    // GET USER BY EMAIL
    // -------------------------
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDTO(user);
    }

    // -------------------------
    // UPDATE ACCESS LEVEL
    // -------------------------
    public void updateAccessLevel(UUID id, int newLevel) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAccessLevel(newLevel);
        userRepository.save(user);
    }

    // -------------------------
    // UPDATE CURRENT USER
    // -------------------------
    public UserDTO updateCurrentUser(UUID userId, UserDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserMapper.updateEntity(user, dto);
        userRepository.save(user);

        return UserMapper.toDTO(user);
    }

    // -------------------------
    // DELETE USER
    // -------------------------
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}