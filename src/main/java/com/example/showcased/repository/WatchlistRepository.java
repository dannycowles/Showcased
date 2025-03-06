package com.example.showcased.repository;

import com.example.showcased.dto.WatchlistReturnDto;
import com.example.showcased.entity.Watchlist;
import com.example.showcased.entity.WatchlistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, WatchlistId> {
    @Query("SELECT new com.example.showcased.dto.WatchlistReturnDto(s.showId, s.title, s.posterPath)" +
            "FROM ShowInfo s JOIN Watchlist w ON s.showId = w.id.showId AND w.id.userId = ?1")
    List<WatchlistReturnDto> findByUserId(Long id);
}
