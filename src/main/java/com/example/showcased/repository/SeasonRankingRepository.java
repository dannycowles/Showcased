package com.example.showcased.repository;

import com.example.showcased.dto.SeasonRankingReturnDto;
import com.example.showcased.entity.SeasonRanking;
import com.example.showcased.entity.SeasonRankingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRankingRepository extends JpaRepository<SeasonRanking, SeasonRankingId> {

    @Query("SELECT MAX(s.rankNum) FROM SeasonRanking s WHERE s.id.userId = :userId")
    Integer findMaxRankNumByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.showcased.dto.SeasonRankingReturnDto(s.id.showId, s.id.season, s.rankNum, s.posterPath) " +
            "FROM SeasonRanking s WHERE s.id.userId = :userId")
    List<SeasonRankingReturnDto> findByIdUserId(@Param("userId") Long userId);
}
