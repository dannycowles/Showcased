package com.example.showcased.repository;

import com.example.showcased.entity.EpisodeRanking;
import com.example.showcased.entity.EpisodeRankingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EpisodeRankingRepository extends JpaRepository<EpisodeRanking, EpisodeRankingId> {
    @Query("SELECT MAX(e.rankNum) FROM EpisodeRanking e WHERE e.id.userId = ?1")
    Integer findMaxRankNumByUserId(Long user);
}
