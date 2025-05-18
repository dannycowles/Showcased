package com.example.showcased.repository;

import com.example.showcased.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserId(Long userId);
    boolean existsByUserIdAndCollectionName(Long userId, String collectionName);
}
