package com.example.showcased.repository;

import com.example.showcased.dto.EpisodeRankingReturnDto;
import com.example.showcased.entity.EpisodeRanking;
import com.example.showcased.entity.EpisodeRankingId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EpisodeRankingRepository extends JpaRepository<EpisodeRanking, EpisodeRankingId> {
    @Query("SELECT MAX(e.rankNum) FROM EpisodeRanking e WHERE e.id.userId = :userId")
    Integer findMaxRankNumByUserId(@Param("userId") Long user);

    @Query("""
        SELECT new com.example.showcased.dto.EpisodeRankingReturnDto(e.id, e.showId, r.rankNum, e.showTitle, e.episodeTitle, e.season, e.episode, e.posterPath)
        FROM EpisodeRanking r
        JOIN EpisodeInfo e ON r.id.episodeId = e.id
        JOIN User u ON r.id.userId = u.id
        WHERE u.displayName = :username
        ORDER BY r.rankNum
    """)
    List<EpisodeRankingReturnDto> findByUsername(@Param("username") String username, Pageable pageable);

    @Query("""
        SELECT COUNT(r)
        FROM EpisodeRanking r
        JOIN User u ON r.id.userId = u.id
        WHERE u.displayName = :username
    """)
    int countByUsername(@Param("username") String username);
}
