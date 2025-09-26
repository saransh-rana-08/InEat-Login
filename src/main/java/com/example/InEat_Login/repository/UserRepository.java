package com.example.InEat_Login.repository;

import com.example.InEat_Login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Method to find a user by their email
    Optional<User> findByEmail(String email);

    // Method to check if a user with an email exists
    boolean existsByEmail(String email);
}