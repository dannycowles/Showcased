package com.example.showcased.repository;

import com.example.showcased.dto.RankingReturnDto;
import com.example.showcased.entity.ShowRanking;
import com.example.showcased.entity.WatchId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowRankingRepository extends JpaRepository<ShowRanking, WatchId> {
    @Query("SELECT MAX(s.rankNum) FROM ShowRanking s WHERE s.id.userId = :userId")
    Integer findMaxRankNumByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT new com.example.showcased.dto.RankingReturnDto(s.showId, r.rankNum, s.title, s.posterPath)
        FROM ShowRanking r
        JOIN ShowInfo s ON r.id.showId = s.showId
        JOIN User u ON r.id.userId = u.id
        WHERE u.displayName = :username
        ORDER BY r.rankNum
    """)
    List<RankingReturnDto> findByUsername(@Param("username") String username, Pageable pageable);

    @Query("""
        SELECT COUNT(r)
        FROM ShowRanking r
        JOIN User u ON r.id.userId = u.id
        WHERE u.displayName = :username
    """)
    int countByUsername(@Param("username") String username);
}
