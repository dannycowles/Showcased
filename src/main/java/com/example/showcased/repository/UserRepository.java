package com.example.showcased.repository;

import com.example.showcased.dto.UserSearchDto;
import com.example.showcased.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByUsernameContainingIgnoreCase(String query);
}
