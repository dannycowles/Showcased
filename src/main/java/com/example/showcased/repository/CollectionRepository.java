package com.example.showcased.repository;

import com.example.showcased.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserId(Long userId);
    boolean existsByUserIdAndCollectionName(Long userId, String collectionName);
    boolean existsByUserIdAndCollectionId(Long userId, Long collectionId);

    List<Collection> findByUserIdAndCollectionNameContainingIgnoreCase(Long userId, String name);
    List<Collection> findByUserIdAndPrivateCollectionFalseAndCollectionNameContainingIgnoreCase(Long userId, String name);
    List<Collection> findByUserIdAndPrivateCollectionFalse(Long userId);
}
