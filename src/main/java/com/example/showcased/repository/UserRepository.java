package com.example.showcased.repository;

import com.example.showcased.dto.ReviewDistributionDto;
import com.example.showcased.entity.User;
import com.example.showcased.service.TMDBClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByDisplayName(String displayName);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByDisplayNameContainingIgnoreCase(String query);

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

    Optional<User> findByDisplayName(String username);

    @Query(value = """
        WITH RECURSIVE rating_values(rating) AS (
            SELECT 1
            UNION ALL
            SELECT rating + 1
            FROM rating_values
            WHERE rating < 10
        ),
        all_reviews AS (
            SELECT ROUND(sr.rating, 0) as rating_rounded
            FROM show_reviews sr
            WHERE sr.user_id = :userId
            UNION ALL
            SELECT ROUND(er.rating, 0) as rating_rounded
            FROM episode_reviews er
            WHERE er.user_id = :userId
        )
        SELECT rv.rating, COUNT(ar.rating_rounded) as numReviews
        FROM rating_values rv
        LEFT JOIN all_reviews ar ON ar.rating_rounded = rv.rating
        GROUP BY rv.rating;
    """, nativeQuery = true)
    List<ReviewDistributionDto> getReviewDistribution(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User u SET u.numReviews = u.numReviews + 1 WHERE u.id = :userId")
    void incrementNumReviews(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE User u SET u.numReviews = u.numReviews - 1 WHERE u.id = :userId")
    void decrementNumReviews(@Param("userId") Long userId);
}
