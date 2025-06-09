package com.example.showcased.repository;

import com.example.showcased.dto.UserSearchDto;
import com.example.showcased.entity.Follower;
import com.example.showcased.entity.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FollowersRepository extends JpaRepository<Follower, FollowerId> {
    Long countByIdFollowingId(Long followingId);
    Long countByIdFollowerId(Long followerId);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.username, u.profilePicture, false, false) " +
            "FROM Follower f JOIN User u ON f.id.followerId = u.id " +
            "WHERE f.id.followingId = :followingId")
    List<UserSearchDto> getFollowersByIdFollowingId(@Param("followingId") Long followingId);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.username, u.profilePicture, false, false) " +
            "FROM Follower f JOIN User u ON f.id.followingId = u.id " +
            "WHERE f.id.followerId = :followerId")
    List<UserSearchDto> getFollowingByIdFollowerId(@Param("followerId") Long followerId);

    @Query("SELECT f.id.followingId " +
            "FROM Follower f " +
            "WHERE f.id.followerId = :userId")
    Set<Long> getFollowingIds(@Param("userId") Long userId);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.username, u.profilePicture, false, false) " +
            "FROM Follower f JOIN User u ON f.id.followerId = u.id " +
            "WHERE f.id.followingId = :userId AND u.username like %:name%")
    List<UserSearchDto> getFollowersByIdFollowingIdFiltered(Long userId, String name);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.username, u.profilePicture, false, false) " +
            "FROM Follower f JOIN User u ON f.id.followingId = u.id " +
            "WHERE f.id.followerId = :userId AND u.username like %:name%")
    List<UserSearchDto> getFollowingByIdFollowerIdFiltered(Long userId, String name);
}
