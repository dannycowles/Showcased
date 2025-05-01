package com.example.showcased.repository;

import com.example.showcased.dto.CharacterRankingReturnDto;
import com.example.showcased.entity.CharacterRanking;
import com.example.showcased.entity.CharacterRankingId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRankingRepository extends JpaRepository<CharacterRanking, CharacterRankingId> {
    @Query("SELECT MAX(c.rankNum) FROM CharacterRanking c WHERE c.id.userId = :userId AND c.characterType = :characterType")
    Integer findMaxRankNumByCharacterType(@Param("userId") Long userId, @Param("characterType") String characterType);

    @Query("SELECT new com.example.showcased.dto.CharacterRankingReturnDto(c.id.characterName, c.showName, c.rankNum) " +
            "FROM CharacterRanking c WHERE c.id.userId =:userId AND c.characterType = :characterType ORDER BY c.rankNum")
    List<CharacterRankingReturnDto> findByIdUserIdAndCharacterType(@Param("userId") Long userId, @Param("characterType") String characterType, Pageable pageRequest);
}
