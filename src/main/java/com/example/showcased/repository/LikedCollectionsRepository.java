package com.example.showcased.repository;

import com.example.showcased.entity.LikedCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedCollectionsRepository extends JpaRepository<LikedCollection, Long> {
    boolean existsByUserIdAndCollectionId(Long userId, Long collectionId);

    Optional<LikedCollection> findByUserIdAndCollectionId(Long userId, Long collectionId);
}
