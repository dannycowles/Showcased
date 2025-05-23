package com.example.showcased.repository;

import com.example.showcased.entity.LikedCollections;
import com.example.showcased.entity.LikedCollectionsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedCollectionsRepository extends JpaRepository<LikedCollections, LikedCollectionsId> {
}
