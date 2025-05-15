package com.example.showcased.repository;

import com.example.showcased.entity.ShowsInCollection;
import com.example.showcased.entity.ShowsInCollectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowsInCollectionsRepository extends JpaRepository<ShowsInCollection, ShowsInCollectionId> {
}
