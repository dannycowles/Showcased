package com.example.showcased.repository;

import com.example.showcased.dto.CharacterRankingReturnDto;
import com.example.showcased.entity.CharacterRanking;
import com.example.showcased.entity.CharacterRankingId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CharacterRankingRepository extends JpaRepository<CharacterRanking, CharacterRankingId> {
    @Query("""
        SELECT MAX(c.rankNum)
        FROM CharacterRanking c
        WHERE c.id.userId = :userId AND c.characterType = :characterType
    """)
    Integer findMaxRankNumByCharacterType(@Param("userId") Long userId, @Param("characterType") String characterType);


    @Query("""
        SELECT new com.example.showcased.dto.CharacterRankingReturnDto(
                c.id.characterId,
                i.showId,
                i.name,
                s.title,
                c.rankNum
            )
            FROM CharacterRanking c
            JOIN CharacterInfo i ON c.id.characterId = i.id
            JOIN ShowInfo s ON i.showId = s.showId
            WHERE c.id.userId = :userId AND c.characterType = :characterType
            ORDER BY c.rankNum
    """)
    List<CharacterRankingReturnDto> findByIdUserIdAndCharacterType(@Param("userId") Long userId, @Param("characterType") String characterType, Pageable pageRequest);
}
