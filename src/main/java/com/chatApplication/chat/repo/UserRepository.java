package com.chatApplication.chat.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.chatApplication.chat.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
