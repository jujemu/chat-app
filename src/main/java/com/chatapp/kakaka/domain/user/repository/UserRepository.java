package com.chatapp.kakaka.domain.user.repository;

import com.chatapp.kakaka.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    @Query("select u from User u where u.role = 'PLUS'")
    List<User> findPlusUsers();
}
