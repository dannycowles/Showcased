package com.example.showcased.repository;

import com.example.showcased.dto.ActivityDto;
import com.example.showcased.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivitiesRepository extends JpaRepository<Activity,Long> {

    @Query("""
        SELECT new com.example.showcased.dto.ActivityDto(
            a.id,
            a.activityType,
            d.description,
            new com.example.showcased.dto.ActivityUserDto(
                u.id,
                u.username,
                u.profilePicture
            ),
            f.createdAt
        )
        FROM Activity a
        JOIN ActivityDescription d ON a.activityType = d.activityType
        JOIN Follower f ON a.externalId = f.id
        JOIN User u ON f.followerId = u.id
        WHERE a.userId = :userId AND a.activityType = 1
        
        ORDER BY f.createdAt DESC
""")
    List<ActivityDto> findByUserId(@Param("userId") Long userId);

    void deleteByExternalId(Long externalId);
}
