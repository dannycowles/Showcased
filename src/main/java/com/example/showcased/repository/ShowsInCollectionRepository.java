package com.example.showcased.repository;

import com.example.showcased.dto.RankingReturnDto;
import com.example.showcased.entity.ShowsInCollection;
import com.example.showcased.entity.ShowsInCollectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowsInCollectionRepository extends JpaRepository<ShowsInCollection, ShowsInCollectionId> {
    @Query("SELECT new com.example.showcased.dto.RankingReturnDto(i.showId, s.rankNum, i.title, i.posterPath) " +
            "FROM Collection c " +
            "JOIN ShowsInCollection s ON c.collectionId = s.id.collectionId " +
            "JOIN ShowInfo i ON s.id.showId = i.showId " +
            "WHERE c.collectionId = :collectionId ORDER BY s.rankNum ASC")
    List<RankingReturnDto> findByIdCollectionId(@Param("collectionId") Long collectionId);
    List<ShowsInCollection> findByIdCollectionIdOrderByRankNumAsc(Long collectionId);

    @Query("SELECT MAX(s.rankNum) FROM ShowsInCollection s WHERE s.id.collectionId = :collectionId")
    Long findMaxRankNumByIdCollectionId(@Param("collectionId") Long collectionId);
}
