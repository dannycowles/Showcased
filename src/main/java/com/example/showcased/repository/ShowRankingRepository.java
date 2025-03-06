package com.example.showcased.repository;

import com.example.showcased.entity.ShowRanking;
import com.example.showcased.entity.WatchId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRankingRepository extends JpaRepository<ShowRanking, WatchId> {
}
