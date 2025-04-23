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

    @Query("SELECT new com.example.showcased.dto.EpisodeRankingReturnDto(r.id.episodeId, e.showId, r.rankNum, e.showTitle, e.episodeTitle, e.season, e.episode, e.posterPath)" +
            "FROM EpisodeInfo e JOIN EpisodeRanking r ON e.id = r.id.episodeId AND r.id.userId = :userId ORDER BY r.rankNum")
    List<EpisodeRankingReturnDto> findByIdUserId(@Param("userId") Long user, Pageable pageable);

    int countByIdUserId(Long userId);
}
