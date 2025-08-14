package com.example.showcased.repository;

import com.example.showcased.dto.WatchReturnDto;
import com.example.showcased.entity.WatchId;
import com.example.showcased.entity.Watching;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WatchingRepository extends JpaRepository<Watching, WatchId> {
    @Query("""
        SELECT new com.example.showcased.dto.WatchReturnDto(s.showId, s.title, s.posterPath)
        FROM Watching w
        JOIN ShowInfo s ON w.id.showId = s.showId
        JOIN User u ON w.id.userId = u.id
        WHERE u.displayName = :username
    """)
    List<WatchReturnDto> findByUsername(@Param("username") String username, Pageable pageable);

    @Query("""
        SELECT COUNT(w)
        FROM Watching w
        JOIN User u ON w.id.userId = u.id
        WHERE u.displayName = :username
    """)
    int countByUsername(@Param("username") String username);
}
