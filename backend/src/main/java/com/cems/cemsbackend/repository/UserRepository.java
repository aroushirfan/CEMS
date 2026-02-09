package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserById(UUID id);
    boolean existsUserByEmail(String email);
    Optional<User> getUserByEmail(String email);
}
