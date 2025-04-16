package com.example.showcased.repository;

import com.example.showcased.dto.UserSearchDto;
import com.example.showcased.entity.Follower;
import com.example.showcased.entity.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowersRepository extends JpaRepository<Follower, FollowerId> {
    Long countByIdFollowingId(Long followingId);
    Long countByIdFollowerId(Long followerId);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.username, u.profilePicture) " +
            "FROM Follower f JOIN User u ON f.id.followerId = u.id " +
            "WHERE f.id.followingId = :followingId")
    List<UserSearchDto> getFollowersByIdFollowingId(@Param("followingId") Long followingId);

    @Query("SELECT new com.example.showcased.dto.UserSearchDto(u.id, u.username, u.profilePicture) " +
            "FROM Follower f JOIN User u ON f.id.followingId = u.id " +
            "WHERE f.id.followerId = :followerId")
    List<UserSearchDto> getFollowingByIdFollowerId(@Param("followerId") Long followerId);
}
