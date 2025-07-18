package com.example.showcased.repository;

import com.example.showcased.dto.ActivityDto;
import com.example.showcased.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivitiesRepository extends JpaRepository<Activity,Long> {

    @Query(value = """
        SELECT new com.example.showcased.dto.ActivityDto(
            a.id,
            a.activityType,
            d.description,
            new com.example.showcased.dto.ActivityUserDto(
                CAST(COALESCE(u1.id, u2.id) AS long),
                COALESCE(u1.username, u2.username),
                COALESCE(u1.profilePicture, u2.profilePicture)
            ),
            new com.example.showcased.dto.ActivityShowReviewLikeDto(
                CASE WHEN a.activityType = 2 THEN lsr.reviewId ELSE NULL END,
                CASE WHEN a.activityType = 2 THEN sr.showId ELSE NULL END,
                CASE WHEN a.activityType = 2 THEN si.title ELSE NULL END
            ),
            COALESCE(f.createdAt, lsr.createdAt)
        )
        FROM Activity a
        JOIN ActivityDescription d ON a.activityType = d.activityType
        LEFT JOIN Follower f ON a.externalId = f.id
        LEFT JOIN User u1 ON f.followerId = u1.id
        LEFT JOIN LikedShowReview lsr ON a.externalId = lsr.id
        LEFT JOIN User u2 ON lsr.userId = u2.id
        LEFT JOIN ShowReview sr ON lsr.reviewId = sr.id
        LEFT JOIN ShowInfo si ON sr.showId = si.showId
        WHERE a.userId = :userId
        ORDER BY COALESCE(f.createdAt, lsr.createdAt) DESC
""")
    List<ActivityDto> findByUserId(@Param("userId") Long userId);

    void deleteByExternalId(Long externalId);
}
