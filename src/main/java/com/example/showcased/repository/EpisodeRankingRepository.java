package com.example.showcased.repository;

import com.example.showcased.dto.EpisodeRankingReturnDto;
import com.example.showcased.entity.EpisodeRanking;
import com.example.showcased.entity.EpisodeRankingId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EpisodeRankingRepository extends JpaRepository<EpisodeRanking, EpisodeRankingId> {
    @Query("SELECT MAX(e.rankNum) FROM EpisodeRanking e WHERE e.id.userId = ?1")
    Integer findMaxRankNumByUserId(Long user);

    @Query("SELECT new com.example.showcased.dto.EpisodeRankingReturnDto(e.id.showId, r.rankNum, e.showTitle, e.episodeTitle, r.id.season, r.id.episode, e.posterPath)" +
            "FROM EpisodeInfo e JOIN EpisodeRanking r ON e.id.showId = r.id.showId AND e.id.season = r.id.season AND e.id.episode = r.id.episode AND r.id.userId = ?1 ORDER BY r.rankNum")
    List<EpisodeRankingReturnDto> findByUserId(Long user);

    @Query("SELECT new com.example.showcased.dto.EpisodeRankingReturnDto(e.id.showId, r.rankNum, e.showTitle, e.episodeTitle, r.id.season, r.id.episode, e.posterPath)" +
            "FROM EpisodeInfo e JOIN EpisodeRanking r ON e.id.showId = r.id.showId AND e.id.season = r.id.season AND e.id.episode = r.id.episode AND r.id.userId = ?1 ORDER BY r.rankNum")
    List<EpisodeRankingReturnDto> findByUserIdTop(Long userId, Pageable pageable);
}
