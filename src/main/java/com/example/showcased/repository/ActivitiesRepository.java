package com.example.showcased.repository;

import com.example.showcased.dto.ActivityDto;
import com.example.showcased.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
                CAST(COALESCE(u1.id, u2.id, u3.id, u4.id, u5.id, u6.id, u7.id, u8.id, u9.id, u10.id, u11.id) AS long),
                COALESCE(u1.displayName, u2.displayName, u3.displayName, u4.displayName, u5.displayName, u6.displayName, u7.displayName, u8.displayName, u9.displayName, u10.displayName, u11.displayName),
                COALESCE(u1.profilePicture, u2.profilePicture, u3.profilePicture, u4.profilePicture, u5.profilePicture, u6.profilePicture, u7.profilePicture, u8.profilePicture, u9.profilePicture, u10.profilePicture, u11.profilePicture)
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
                CASE WHEN a.activityType = 5 THEN erc.id ELSE NULL END,
                CASE WHEN a.activityType = 5 THEN erc.commentText ELSE NULL END
            ),
            new com.example.showcased.dto.ActivityShowReviewCommentLikeDto(
                CASE WHEN a.activityType = 6 THEN lsrc.commentId ELSE NULL END,
                CASE WHEN a.activityType = 6 THEN src2.reviewId ELSE NULL END,
                CASE WHEN a.activityType = 6 THEN si3.showId ELSE NULL END,
                CASE WHEN a.activityType = 6 THEN si3.title ELSE NULL END,
                CASE WHEN a.activityType = 6 THEN src2.commentText ELSE NULL END,
                CASE WHEN a.activityType = 6 THEN u6b.id ELSE NULL END,
                CASE WHEN a.activityType = 6 THEN u6b.displayName ELSE NULL END,
                CASE WHEN a.activityType = 6 THEN u6b.profilePicture ELSE NULL END,
                CASE WHEN a.activityType = 6 THEN (u6b.id = :userId) ELSE NULL END
            ),
            new com.example.showcased.dto.ActivityEpisodeReviewCommentLikeDto(
                CASE WHEN a.activityType = 7 THEN erc2.reviewId ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN ei3.showId ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN ei3.showTitle ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN ei3.season ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN ei3.episode ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN ei3.episodeTitle ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN lerc.commentId ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN erc2.commentText ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN u7b.id ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN u7b.displayName ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN u7b.profilePicture ELSE NULL END,
                CASE WHEN a.activityType = 7 THEN (u7b.id = :userId) ELSE NULL END
            ),
            new com.example.showcased.dto.ActivityCollectionLikeDto(
                CASE WHEN a.activityType = 8 THEN lc.collectionId ELSE NULL END,
                CASE WHEN a.activityType = 8 THEN c.collectionName ELSE NULL END
            ),
            new com.example.showcased.dto.ActivitySeasonReviewLikeDto(
                CASE WHEN a.activityType = 9 THEN sesr.id ELSE NULL END,
                CASE WHEN a.activityType = 9 THEN sesr.showId ELSE NULL END,
                CASE WHEN a.activityType = 9 THEN si4.showTitle ELSE NULL END,
                CASE WHEN a.activityType = 9 THEN si4.season ELSE NULL END
            ),
            new com.example.showcased.dto.ActivitySeasonReviewCommentDto(
                CASE WHEN a.activityType = 10 THEN sesr2.id ELSE NULL END,
                CASE WHEN a.activityType = 10 THEN sesr2.showId ELSE NULL END,
                CASE WHEN a.activityType = 10 THEN si5.showTitle ELSE NULL END,
                CASE WHEN a.activityType = 10 THEN si5.season ELSE NULL END,
                CASE WHEN a.activityType = 10 THEN sesrc.id ELSE NULL END
            ),
            new com.example.showcased.dto.ActivitySeasonReviewCommentLikeDto(
               CASE WHEN a.activityType = 11 THEN sesr3.id ELSE NULL END,
               CASE WHEN a.activityType = 11 THEN sesr3.showId ELSE NULL END,
               CASE WHEN a.activityType = 11 THEN si6.showTitle ELSE NULL END,
               CASE WHEN a.activityType = 11 THEN si6.season ELSE NULL END,
               CASE WHEN a.activityType = 11 THEN lsrc2.commentId ELSE NULL END,
               CASE WHEN a.activityType = 11 THEN u11b.id ELSE NULL END,
               CASE WHEN a.activityType = 11 THEN u11b.displayName ELSE NULL END,
               CASE WHEN a.activityType = 11 THEN u11b.profilePicture ELSE NULL END,
               CASE WHEN a.activityType = 11 THEN (u11b.id = :userId) ELSE NULL END
            ),
            COALESCE(f.createdAt, lsr.createdAt, src.createdAt, ler.createdAt, erc.createdAt, lsrc.createdAt, lerc.createdAt, lc.createdAt, lsesr.createdAt, sesrc.createdAt, lsrc2.createdAt)
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
        
        LEFT JOIN LikedShowReviewComment lsrc ON a.externalId = lsrc.id AND a.activityType = 6
        LEFT JOIN User u6 ON lsrc.userId = u6.id
        LEFT JOIN ShowReviewComment src2 ON lsrc.commentId = src2.id
        LEFT JOIN ShowReview sr3 ON src2.reviewId = sr3.id
        LEFT JOIN ShowInfo si3 ON sr3.showId = si3.showId
        LEFT JOIN User u6b ON sr3.userId = u6b.id
        
        LEFT JOIN LikedEpisodeReviewComment lerc ON a.externalId = lerc.id AND a.activityType = 7
        LEFT JOIN User u7 ON lerc.userId = u7.id
        LEFT JOIN EpisodeReviewComment erc2 ON lerc.commentId = erc2.id
        LEFT JOIN EpisodeReview er3 ON erc2.reviewId = er3.id
        LEFT JOIN EpisodeInfo ei3 ON er3.episodeId = ei3.id
        LEFT JOIN User u7b ON er3.userId = u7b.id
        
        LEFT JOIN LikedCollection lc ON a.externalId = lc.id AND a.activityType = 8
        LEFT JOIN User u8 ON lc.userId = u8.id
        LEFT JOIN Collection c ON lc.collectionId = c.collectionId
        
        LEFT JOIN LikedSeasonReview lsesr ON a.externalId = lsesr.id AND a.activityType = 9
        LEFT JOIN User u9 ON lsesr.userId = u9.id
        LEFT JOIN SeasonReview sesr ON lsesr.reviewId = sesr.id
        LEFT JOIN SeasonInfo si4 ON sesr.seasonId = si4.id
        
        LEFT JOIN SeasonReviewComment sesrc ON a.externalId = sesrc.id AND a.activityType = 10
        LEFT JOIN User u10 ON sesrc.userId = u10.id
        LEFT JOIN SeasonReview sesr2 ON sesrc.reviewId = sesr2.id
        LEFT JOIN SeasonInfo si5 ON sesr2.seasonId = si5.id
        
        LEFT JOIN LikedSeasonReviewComment lsrc2 ON a.externalId = lsrc2.id AND a.activityType = 11
        LEFT JOIN User u11 ON lsrc2.userId = u11.id
        LEFT JOIN SeasonReviewComment sesrc2 ON lsrc2.commentId = sesrc2.id
        LEFT JOIN SeasonReview sesr3 ON sesrc2.reviewId = sesr3.id
        LEFT JOIN SeasonInfo si6 ON sesr3.seasonId = si6.id
        LEFT JOIN User u11b ON sesr3.userId = u11b.id
        
        WHERE a.userId = :userId
        ORDER BY COALESCE(f.createdAt, lsr.createdAt, src.createdAt, ler.createdAt, erc.createdAt, lsrc.createdAt, lerc.createdAt, lc.createdAt, lsesr.createdAt, sesrc.createdAt, lsrc2.createdAt) DESC
""")
    Page<ActivityDto> findByUserId(@Param("userId") Long userId, Pageable page);

    void deleteByExternalIdAndActivityType(Long id, int type);

    @Modifying
    @Query("""
        DELETE FROM Activity a
        WHERE (
            a.externalId IN (
                SELECT lr.id FROM LikedShowReview lr WHERE lr.reviewId = :reviewId
            )
            OR
            a.externalId IN (
                SELECT src.id FROM ShowReviewComment src WHERE src.reviewId = :reviewId
            )
        )
        AND a.activityType IN :activityTypes
""")
    void deleteShowReviewActivities(@Param("reviewId") Long reviewId, @Param("activityTypes") List<Integer> activityTypes);

    @Modifying
    @Query("""
        DELETE FROM Activity a
        WHERE (
            a.externalId IN (
                SELECT lr.id FROM LikedEpisodeReview lr WHERE lr.reviewId = :reviewId
            )
            OR
            a.externalId IN (
                SELECT erc.id FROM EpisodeReviewComment erc WHERE erc.reviewId = :reviewId
            )
        )
        AND a.activityType IN :activityTypes
""")
    void deleteEpisodeReviewActivities(@Param("reviewId") Long reviewId, @Param("activityTypes") List<Integer> activityTypes);

    @Modifying
    @Query("""
        DELETE FROM Activity a
        WHERE (
            a.externalId IN (
                SELECT lr.id FROM LikedSeasonReview lr WHERE lr.reviewId = :reviewId
            )
            OR
            a.externalId IN (
                SELECT rc.id FROM SeasonReviewComment rc WHERE rc.reviewId = :reviewId
            )
        )
""")
    void deleteSeasonReviewActivities(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("""
        DELETE FROM Activity a
        WHERE (
            a.externalId IN (
                SELECT lrc.id FROM LikedShowReviewComment lrc WHERE lrc.commentId = :commentId
            )
            OR
            a.externalId IN (
                SELECT rc.id FROM ShowReviewComment rc WHERE rc.id = :commentId
            )
        )
        AND a.activityType IN :activityTypes
""")
    void deleteShowReviewCommentActivities(@Param("commentId") Long commentId, @Param("activityTypes") List<Integer> activityTypes);

    @Modifying
    @Query("""
       DELETE FROM Activity a
       WHERE (
           a.externalId IN (
                SELECT lrc.id FROM LikedEpisodeReviewComment lrc WHERE lrc.commentId = :commentId
           )
           OR
           a.externalId IN (
                SELECT rc.id FROM EpisodeReviewComment rc WHERE rc.id = :commentId
           )
       )
       AND a.activityType IN :activityTypes
""")
    void deleteEpisodeReviewCommentActivities(@Param("commentId") Long commentId, @Param("activityTypes") List<Integer> activityTypes);

    @Modifying
    @Query("""
        DELETE FROM Activity a
        WHERE (
            a.externalId IN (
                SELECT lrc.id FROM LikedSeasonReviewComment lrc WHERE lrc.commentId = :commentId
            )
            OR
            a.externalId IN (
                SELECT rc.id FROM SeasonReviewComment rc WHERE rc.id = :commentId
            )
        )
""")
    void deleteSeasonReviewCommentActivities(@Param("commentId") Long commentId);
}
