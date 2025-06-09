package com.example.showcased.repository;

import com.example.showcased.dto.SeasonRankingReturnDto;
import com.example.showcased.entity.SeasonRanking;
import com.example.showcased.entity.SeasonRankingId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeasonRankingRepository extends JpaRepository<SeasonRanking, SeasonRankingId> {

    @Query("SELECT MAX(s.rankNum) FROM SeasonRanking s WHERE s.id.userId = :userId")
    Integer findMaxRankNumByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.showcased.dto.SeasonRankingReturnDto(i.id, i.showId, i.season, s.rankNum, i.posterPath, i.showTitle) " +
            "FROM SeasonRanking s JOIN SeasonInfo i ON s.id.seasonId = i.id " +
            "WHERE s.id.userId = :userId ORDER BY s.rankNum")
    List<SeasonRankingReturnDto> findByIdUserId(@Param("userId") Long userId, Pageable pageable);

    int countByIdUserId(Long userId);
}
