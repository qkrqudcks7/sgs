package com.example.sgsparks.repository;

import com.example.sgsparks.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
//    Optional<User> findByEmailAndPassword(String userId, String password);
    Optional<User> findByEmail(String email);
}
