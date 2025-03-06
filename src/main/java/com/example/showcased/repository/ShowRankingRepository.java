package com.example.showcased.repository;

import com.example.showcased.dto.RankingReturnDto;
import com.example.showcased.entity.ShowRanking;
import com.example.showcased.entity.WatchId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRankingRepository extends JpaRepository<ShowRanking, WatchId> {
    @Query("SELECT MAX(s.rankNum) FROM ShowRanking s WHERE s.id.userId = ?1")
    Integer findMaxRankNumByUserId(Long userId);

    @Query("SELECT new com.example.showcased.dto.RankingReturnDto(s.showId, r.rankNum, s.title, s.posterPath)" +
            "FROM ShowInfo s JOIN ShowRanking r ON s.showId = r.id.showId AND r.id.userId = ?1 ORDER BY r.rankNum")
    List<RankingReturnDto> findByUserIdOrderByRankNumDesc(Long user);
}
