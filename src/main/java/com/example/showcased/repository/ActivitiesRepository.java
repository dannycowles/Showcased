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
                CAST(COALESCE(u1.id, u2.id, u3.id, u4.id, u5.id) AS long),
                COALESCE(u1.username, u2.username, u3.username, u4.username, u5.username),
                COALESCE(u1.profilePicture, u2.profilePicture, u3.profilePicture, u4.profilePicture, u5.profilePicture)
            ),
            new com.example.showcased.dto.ActivityShowReviewLikeDto(
                CASE WHEN a.activityType = 2 THEN lsr.reviewId ELSE NULL END,
                CASE WHEN a.activityType = 2 THEN sr.showId ELSE NULL END,
                CASE WHEN a.activityType = 2 THEN si.title ELSE NULL END
            ),
            new com.example.showcased.dto.ActivityShowReviewCommentDto(
                CASE WHEN a.activityType = 3 THEN src.id ELSE NULL END,
                CASE WHEN a.activityType = 3 THEN sr2.id ELSE NULL END,
                CASE WHEN a.activityType = 3 THEN sr2.showId ELSE NULL END,
                CASE WHEN a.activityType = 3 THEN si2.title ELSE NULL END,
                CASE WHEN a.activityType = 3 THEN src.commentText ELSE NULL END
            ),
            new com.example.showcased.dto.ActivityEpisodeReviewLikeDto(
                CASE WHEN a.activityType = 4 THEN ler.reviewId ELSE NULL END,
                CASE WHEN a.activityType = 4 THEN ei.showId ELSE NULL END,
                CASE WHEN a.activityType = 4 THEN ei.showTitle ELSE NULL END,
                CASE WHEN a.activityType = 4 THEN ei.season ELSE NULL END,
                CASE WHEN a.activityType = 4 THEN ei.episode ELSE NULL END,
                CASE WHEN a.activityType = 4 THEN ei.episodeTitle ELSE NULL END
            ),
            new com.example.showcased.dto.ActivityEpisodeReviewCommentDto(
                CASE WHEN a.activityType = 5 THEN erc.reviewId ELSE NULL END,
                CASE WHEN a.activityType = 5 THEN ei2.showId ELSE NULL END,
                CASE WHEN a.activityType = 5 THEN ei2.showTitle ELSE NULL END,
                CASE WHEN a.activityType = 5 THEN ei2.season ELSE NULL END,
                CASE WHEN a.activityType = 5 THEN ei2.episode ELSE NULL END,
                CASE WHEN a.activityType = 5 THEN ei2.episodeTitle ELSE NULL END,
                CASE WHEN a.activityType = 5 THEN erc.commentText ELSE NULL END
            ),
            COALESCE(f.createdAt, lsr.createdAt, src.createdAt, ler.createdAt, erc.createdAt)
        )
        FROM Activity a
        JOIN ActivityDescription d ON a.activityType = d.activityType
        
        LEFT JOIN Follower f ON a.externalId = f.id AND a.activityType = 1
        LEFT JOIN User u1 ON f.followerId = u1.id
        
        LEFT JOIN LikedShowReview lsr ON a.externalId = lsr.id AND a.activityType = 2
        LEFT JOIN User u2 ON lsr.userId = u2.id
        LEFT JOIN ShowReview sr ON lsr.reviewId = sr.id
        LEFT JOIN ShowInfo si ON sr.showId = si.showId
        
        LEFT JOIN ShowReviewComment src ON a.externalId = src.id AND a.activityType = 3
        LEFT JOIN User u3 ON src.userId = u3.id
        LEFT JOIN ShowReview sr2 ON src.reviewId = sr2.id
        LEFT JOIN ShowInfo si2 ON sr2.showId = si2.showId
        
        LEFT JOIN LikedEpisodeReview ler ON a.externalId = ler.id AND a.activityType = 4
        LEFT JOIN User u4 ON ler.userId = u4.id
        LEFT JOIN EpisodeReview er ON ler.reviewId = er.id
        LEFT JOIN EpisodeInfo ei ON er.episodeId = ei.id
        
        LEFT JOIN EpisodeReviewComment erc ON a.externalId = erc.id AND a.activityType = 5
        LEFT JOIN User u5 ON erc.userId = u5.id
        LEFT JOIN EpisodeReview er2 ON erc.reviewId = er2.id
        LEFT JOIN EpisodeInfo ei2 ON er2.episodeId = ei2.id
        
        WHERE a.userId = :userId
        ORDER BY COALESCE(f.createdAt, lsr.createdAt, src.createdAt, ler.createdAt, erc.createdAt) DESC
""")
    List<ActivityDto> findByUserId(@Param("userId") Long userId);

    void deleteByExternalIdAndActivityType(Long id, int i);
}
