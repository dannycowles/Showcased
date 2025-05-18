package com.example.showcased.repository;

import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.entity.ShowsInCollection;
import com.example.showcased.entity.ShowsInCollectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowsInCollectionRepository extends JpaRepository<ShowsInCollection, ShowsInCollectionId> {
    @Query("SELECT new com.example.showcased.dto.WatchReturnDto(i.showId, i.title, i.posterPath) " +
            "FROM Collection c " +
            "JOIN ShowsInCollection s ON c.collectionId = s.id.collectionId " +
            "JOIN ShowInfo i ON s.id.showId = i.showId " +
            "WHERE c.collectionId = :collectionId")
    List<WatchReturnDto> findByIdCollectionId(@Param("collectionId") Long collectionId);
}
