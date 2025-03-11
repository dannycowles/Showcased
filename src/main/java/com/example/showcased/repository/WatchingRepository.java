package com.example.showcased.repository;

import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.entity.WatchId;
import com.example.showcased.entity.Watching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchingRepository extends JpaRepository<Watching, WatchId> {
    @Query("SELECT new com.example.showcased.dto.WatchReturnDto(s.showId, s.title, s.posterPath)" +
            "FROM ShowInfo s JOIN Watching w ON s.showId = w.id.showId AND w.id.userId = ?1")
    List<WatchReturnDto> findByUserId(Long id);

    @Query("SELECT new com.example.showcased.dto.WatchReturnDto(s.showId, s.title, s.posterPath)" +
            "FROM ShowInfo s JOIN Watching w ON s.showId = w.id.showId AND w.id.userId = ?1")
    List<WatchReturnDto> findByUserIdTop(Long user, Pageable pageRequest);
}
