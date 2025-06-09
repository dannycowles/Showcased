package com.example.showcased.repository;

import com.example.showcased.entity.LikedCollections;
import com.example.showcased.entity.LikedCollectionsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedCollectionsRepository extends JpaRepository<LikedCollections, LikedCollectionsId> {
    int countByIdCollectionId(Long collectionId);
}
