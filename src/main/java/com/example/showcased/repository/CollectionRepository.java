package com.example.showcased.repository;

import com.example.showcased.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserId(Long userId);
    boolean existsByUserIdAndCollectionName(Long userId, String collectionName);
    boolean existsByUserIdAndCollectionId(Long userId, Long collectionId);

    @Query("SELECT c FROM Collection c WHERE c.userId = :userId AND c.isPrivate = false")
    List<Collection> findByUserIdPublic(@Param("userId") Long userId);
}
