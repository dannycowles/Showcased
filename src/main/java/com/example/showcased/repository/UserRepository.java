package com.example.showcased.repository;

import com.example.showcased.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByUsernameContainingIgnoreCase(String query);

    @Modifying
    @Query("UPDATE User u SET u.numFollowers = u.numFollowers + 1 WHERE u.id = :userId")
    void incrementFollowersCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User u SET u.numFollowing = u.numFollowing + 1 WHERE u.id = :userId")
    void incrementFollowingCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User u SET u.numFollowers = u.numFollowers - 1 WHERE u.id = :userId")
    void decrementFollowersCount(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User u SET u.numFollowing = u.numFollowing - 1 WHERE u.id = :userId")
    void decrementFollowingCount(@Param("userId") Long userId);
}
