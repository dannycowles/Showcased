package com.example.showcased.repository;

import com.example.showcased.dto.DynamicRankingReturnDto;
import com.example.showcased.entity.DynamicRanking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DynamicRankingRepository extends JpaRepository<DynamicRanking, Long> {
    @Query("""
        SELECT new com.example.showcased.dto.DynamicRankingReturnDto(
            d.id,
            c1.id,
            c1.name,
            c2.id,
            c2.name,
            s.title,
            d.rankNum
        )
        FROM DynamicRanking d
        JOIN CharacterInfo c1 ON d.character1Id = c1.id
        JOIN CharacterInfo c2 ON d.character2Id = c2.id
        JOIN ShowInfo s ON c1.showId = s.showId
        WHERE d.userId = :userId
""")
    List<DynamicRankingReturnDto> findByIdUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT MAX(d.rankNum) FROM DynamicRanking d WHERE d.userId = :userId")
    Integer findMaxRankNumByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndCharacter1IdAndCharacter2Id(Long userId, String character1Id, String character2Id);
}
