package com.example.showcased.repository;

import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.entity.Watchlist;
import com.example.showcased.entity.WatchId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, WatchId> {
    @Query("SELECT new com.example.showcased.dto.WatchReturnDto(s.showId, s.title, s.posterPath)" +
            "FROM ShowInfo s JOIN Watchlist w ON s.showId = w.id.showId AND w.id.userId = :userId")
    List<WatchReturnDto> findByIdUserId(@Param("userId") Long userId, Pageable pageable);

    int countByIdUserId(Long userId);
}
