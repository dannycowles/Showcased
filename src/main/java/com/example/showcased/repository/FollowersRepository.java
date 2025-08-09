package com.example.showcased.repository;

import com.example.showcased.dto.UserSearchDto;
import com.example.showcased.entity.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface FollowersRepository extends JpaRepository<Follower, Long> {
    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.displayName, u.profilePicture, false, false) " +
            "FROM Follower f JOIN User u ON f.followerId = u.id " +
            "WHERE f.followingId = :followingId")
    Page<UserSearchDto> getFollowersByIdFollowingId(@Param("followingId") Long followingId, Pageable page);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.displayName, u.profilePicture, false, false) " +
            "FROM Follower f JOIN User u ON f.followingId = u.id " +
            "WHERE f.followerId = :followerId")
    Page<UserSearchDto> getFollowingByIdFollowerId(@Param("followerId") Long followerId, Pageable page);

    @Query("SELECT f.followingId " +
            "FROM Follower f " +
            "WHERE f.followerId = :userId")
    Set<Long> getFollowingIds(@Param("userId") Long userId);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.displayName, u.profilePicture, false, false) " +
            "FROM Follower f JOIN User u ON f.followerId = u.id " +
            "WHERE f.followingId = :userId AND u.displayName like %:name%")
    Page<UserSearchDto> getFollowersByIdFollowingIdFiltered(Long userId, String name, Pageable page);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.displayName, u.profilePicture, false, false) " +
            "FROM Follower f JOIN User u ON f.followingId = u.id " +
            "WHERE f.followerId = :userId AND u.displayName like %:name%")
    Page<UserSearchDto> getFollowingByIdFollowerIdFiltered(Long userId, String name, Pageable page);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follower> findByFollowerIdAndFollowingId(Long userId, Long unfollowId);
}
